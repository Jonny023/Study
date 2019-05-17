package com.prospect.utils

import com.aliyun.oss.OSSClient
import com.prospect.dolphin.ConfigUtils

import com.prospect.dolphin.JsonUtils
import com.prospect.dolphin.MD5Utils
import com.prospect.dolphin.Order
import com.prospect.dolphin.UUIDUtils
import com.prospect.dolphin.exception.MyException
import com.prospect.dolphin.exception.ResultEnum
import grails.util.Environment
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContexts
import org.apache.http.util.EntityUtils

import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.lang.reflect.Field
import java.security.KeyStore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException


class WeChatService {

    def redisService
    def userService
    def httpService
    def aliOssService
    def orderService
    def integralService
    def integralRecordService

    final String APP_ID          = ConfigUtils.WX_APP_ID       				             // 微信分配的公众号ID（开通公众号之后可以获取到）
    final String APP_SECRET      = ConfigUtils.WX_APP_SECRET   				             // 微信分配的公众号ID（开通公众号之后可以获取到）
    final String MCH_ID          = ConfigUtils.WX_MCH_ID       				             // 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
    final String KEY             = ConfigUtils.WX_KEY          				             // 支付签名key
    final String CERT_PASSWORD   = ConfigUtils.WX_CERT_PASSWORD				             // HTTPS证书密码，默认密码等于商户号MCHID

    final String HTTP_TPYE       = ConfigUtils.WX_HTTP_TPYE
    final String DOMAIN_NAME     = ConfigUtils.WX_DOMAIN_NAME
    final String CRET_LOCAL_PATH = ConfigUtils.WX_CRET_LOCAL_PATH		                 // HTTPS证书的本地路径
    final String SCENE_ID        = ConfigUtils.WX_SCENE_ID       			             // 发放红包使用场景，红包金额大于200时必传  PRODUCT_1:商品促销 PRODUCT_2:抽奖 PRODUCT_3:虚拟物品兑奖 PRODUCT_4:企业内部福利  PRODUCT_5:渠道分润 PRODUCT_6:保险回馈 PRODUCT_7:彩票派奖 PRODUCT_8:税务刮奖
    final String NOTIFY_URL      = HTTP_TPYE + DOMAIN_NAME + "ht_srv" + ConfigUtils.WX_NOTIFY_URL    // 微信回调地址https最好
    final String IP              = ConfigUtils.WX_IP

    /**
     * 生成预支付订单并生成jsAPIOrder
     * @param outTradeNo    订单id
     * @param totalFee      支付金额
     * @param openId        用户的openid
     * @param body          备注 默认为空
     * @param tradeType     JSAPI，NATIVE，APP，默认JSAPI
     * @return
     */
    def pay(String outTradeNo,Integer totalFee,String openId, String body = "", String tradeType = "JSAPI"){
        def unifiedorder = unifiedorder(outTradeNo, totalFee, body, openId, tradeType)
        def unifiedXNL = httpsRequestPost("https://api.mch.weixin.qq.com/pay/unifiedorder", mapToXmlStr(unifiedorder))
        if (unifiedXNL) {
            def unifiedMap = xmlStrToMap(unifiedXNL)
            if (unifiedMap.return_code.text() == "FAIL") {
                throw new MyException(580, "wechat is error", "签名错误")
            } else {
                return jsAPIOrder((String) unifiedMap.prepay_id.text())
            }
        } else {
            throw new MyException(440, "unified is error", "预支付订单生成失败")
        }
    }

    /**
     * 获取微信回调信息
     * @param request
     * @return
     */
    def notice(String notityXml) {
//        String notityXml = request.getXML()
        if(notityXml == null || notityXml.length() == 0){
            return null
        }else{
            def notityMap = xmlStrToMap(notityXml)
            def res = [
                    "isResult":notityMap.result_code.text() == "SUCCESS",
                    "isReturn":notityMap.return_code.text() == "SUCCESS",
                    "outTradeNo":notityMap.out_trade_no.text()
            ]
            return res
        }
    }

    /**
     * 退款
     * @param outTradeNo
     * @param amount
     * @return
     */
    def refundByOutTradeNo(String outTradeNo, Integer amount) {
        def refundMXL = mapToXmlStr(refundOrder(outTradeNo, amount, amount))
        String refundMap = xmlStrToMap(refundMXL)
        if (refundMap.return_msg.text() == "OK" && refundMap.return_code.text() == "SUCCESS" && refundMap.result_code.text() == "SUCCESS") {
            return true
        } else {
            return false
        }
    }

    /**
     * 企业付款
     * @param param
     * NO_CHECK：不校验真实姓名
     * FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账）
     * OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
     * @return
     */
    boolean httpsEnterprisePay(String openid, String partnerTradeNo,String checkName, String reUserName, Integer amount, String desc){
        if(openid == null || partnerTradeNo == null || checkName == null || amount == null || desc == null ){
            return false
        }else{
            String requestUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers"
            def map = httpSSL(requestUrl, enterprisePay(openid,partnerTradeNo,checkName,reUserName,amount,desc))
            if(map.return_code.text() == "SUCCESS"){
                if(map.result_code.text() == "SUCCESS"){
                    return true
                }else if(map.result_code.text() == "FAIL"){
                    log.error("企业支付失败 err_code: " +map.err_code.text() + "  err_code_des:  "+ map.err_code_des.text())
                    return false
                }
            }else if(map.return_code.text() == "FAIL"){
                log.error("企业支付失败 msg: " + map.return_msg.text())
                return false
            }


// --------------------------旧版本------------------------------------------------------------
//            def enterprisePay = enterprisePay(openid,partnerTradeNo,checkName,reUserName,amount,desc)
//            KeyStore keyStore = KeyStore.getInstance("PKCS12");
//            FileInputStream instream = new FileInputStream(new File(CRET_LOCAL_PATH));//加载本地的证书进行https加密传输
//            try {
//                //指定PKCS12的密码(商户ID)
//                keyStore.load(instream, CERT_PASSWORD.toCharArray());//设置证书密码
//                SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, CERT_PASSWORD.toCharArray()).build();
//                String[] supportProtocals = new String[1]
//                supportProtocals[0] = "TLSv1"
//                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, supportProtocals, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//                CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
//                HttpPost httpPost = new HttpPost(requestUrl);
//                //将要提交给API的数据对象转换成XML格式数据Post给API
//                httpPost.addHeader("Content-Attr", "text/xml");
//                def param = mapToXmlStr(enterprisePay)
//                httpPost.setEntity(new StringEntity(param, "UTF-8"));
//                String result=null;
//                try {
//                    HttpResponse response = httpClient.execute(httpPost);
//                    HttpEntity entity = response.getEntity();
//                    result = EntityUtils.toString(entity, "UTF-8");
//                    httpPost.abort();
//                    //解析xml成map
//                    def map = new XmlParser().parseText(result)
//                    if(map.return_code.text() == "SUCCESS"){
//                        if(map.result_code.text() == "SUCCESS"){
//                            return true
//                        }else if(map.result_code.text() == "FAIL"){
//                            log.error("企业支付失败 err_code: " +map.err_code.text() + "  err_code_des:  "+ map.err_code_des.text())
//                            return false
//                        }
//                    }else if(map.return_code.text() == "FAIL"){
//                        log.error("企业支付失败 msg: " + map.return_msg.text())
//                        return false
//                    }
//                } catch (Exception e) {
//                    log.error("httpClient execute exception: " + e.getMessage())
//                    return false;
//                }
//            } catch (CertificateException e) {
//                log.error("keyStore.load execute exception: " + e.getMessage())
//                return false;
//            } catch (NoSuchAlgorithmException e) {
//                log.error("keyStore.load execute exception: " + e.getMessage())
//                return false;
//            } finally {
//                instream.close();
//            }
        }
    }


    /**
     * 企业发放红包
     * @param name      红包发送者名称
     * @param openid    接受红包的用户
     * @param amount    付款金额，单位分
     * @param wishing   红包祝福语
     * @param actName   活动名称
     * @param remark    备注信息
     * @return
     */
    boolean sendredpack(String name, String openid, Integer amount, String wishing, String actName, String remark){
        String requestUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack"
        def map = httpSSL(requestUrl, redpack(name, openid, amount, wishing, actName, remark))
        if(map){
            if(map.return_code.text() == "SUCCESS"){
                if(map.result_code.text() == "SUCCESS"){
                    return true
                }else if(map.result_code.text() == "FAIL"){
                    log.error("红包发送失败 err_code: " +map.err_code.text() + "  err_code_des:  "+ map.err_code_des.text())
                    return false
                }
            }else if(map.return_code.text() == "FAIL"){
                log.error("红包发送失败 msg: " + map.return_msg.text())
                return false
            }
        }else{
            return false
        }
    }


    /**	------------------------------------------------------------------------------------------------------------------------
     * 根据参数获取 得到code的连接
     * @author zyw
     * @param redirect_url	回调地址
     * @param scope			请求授权方式		snsapi_base静默授权	snsapi_userinfo需用户同意授权
     *                                      snsapi_base此参数获取不再提供unionid,请注意
     * @return 	替换好的请求code的url
     */
    String getCodeUrl(String redirect_url, String scope){//snsapi_userinfo  //snsapi_base
        if(!scope) scope = "snsapi_userinfo"
        if(!redirect_url) throw new MyException(ResultEnum.PARAMS_ERROR)
        String getCodeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
        getCodeUrl  = getCodeUrl.replace("APPID", URLEncoder.encode(APP_ID,"UTF-8"))
        getCodeUrl  = getCodeUrl.replace("REDIRECT_URI",URLEncoder.encode(redirect_url,"UTF-8"))
        getCodeUrl  = getCodeUrl.replace("SCOPE", URLEncoder.encode(scope,"UTF-8"))
        return getCodeUrl
    }

    /**	------------------------------------------------------------------------------------------------------------------------
     * 根据code获取用户的openId
     * @author zyw
     * @param code				获取的code
     * @return	{"access_token":"3vwHoAuVcZZG6jcyB_SrU83Tmie_nVt3I8aMpcptlX-u4GK1B7NyYqoC_B5WFtW0KCstu5le7TZWAK7gaMKf2lykyzQiWvcAPxThKDqujl8","expires_in":7200,"refresh_token":"w8DhOa-6_hSiuXatmMl0ZSoskoKQC1LR5JypOUSiAFwWlo78xvuzhzLWBfU_6USJJgsGRJ5Ljp8gHp4ncHdHqne6N3ZQBsHawvoyRRbaOws","openid":"oyepit9Q0xlK4HhaY2_ETYguIphA","scope":"snsapi_userinfo"}
     */
    def getOauth(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code"
        url = url.replace("CODE",code)
        url = url.replace("APPID", APP_ID)
        url = url.replace("SECRET", APP_SECRET)
        return httpService.postJson(url).entity
    }

    /**	------------------------------------------------------------------------------------------------------------------------
     * 获取	access_token
     * @author zyw
     * @return 返回access_token
     */
    String getAccessToken(){
		if(redisService.exists("wechat_access_token")){
			return redisService.get("wechat_access_token");
		}else{
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET"
            try {
                url  = url.replace("APPID", APP_ID)
                url = url.replace("SECRET", APP_SECRET)
                def entity = httpService.postJson(url).entity
                String access_token = entity.access_token
                redisService.setex("wechat_access_token", 60 * 30, access_token)
                return access_token
            } catch (UnsupportedEncodingException e2) {
                throw new MyException(440, "params is error", "参数错误")
            }
		}
    }

    String getAccessToken(String appid, String secret){
          String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET"
        try {
            url  = url.replace("APPID", URLEncoder.encode(appid,"UTF-8"))
            url  = url.replace("SECRET", URLEncoder.encode(secret,"UTF-8"))
            def entity = httpService.postJson(url).entity
            return entity.access_token
        } catch (UnsupportedEncodingException e2) {
            throw new MyException(440, "params is error", "参数错误")
        }
    }


    def getMedia(String mediaId){
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID"
        url = url.replace("ACCESS_TOKEN",getAccessToken())
        url = url.replace("MEDIA_ID", mediaId)

        URL u = new URL(url)
        HttpURLConnection conn = (HttpURLConnection) u.openConnection()
        conn.setRequestMethod("GET")
        conn.setDoInput(true)
        conn.setDoOutput(true)
        InputStream inputStream = conn.getInputStream()

        def res = [mp3: "", amr: ""]
        String name = UUIDUtils.createUUID()
        OSSClient ossClient = new OSSClient(aliOssService.http + aliOssService.endpoint, aliOssService.accessKeyId, aliOssService.accessKeySecret)
        if(ossClient && inputStream && name){
            try {
                ossClient.putObject("dolphin-amr", name + ".amr", inputStream)
                res.mp3 = aliOssService.http + "dolphin-mp3" + "." + aliOssService.endpoint + File.separator + name + ".mp3"
                res.amr = aliOssService.http + "dolphin-amr" + "." + aliOssService.endpoint + File.separator + name + ".amr"
            }finally {
                ossClient.shutdown()
            }
        }else{

        }
        return res
    }


    /**	------------------------------------------------------------------------------------------------------------------------
     * 获取
     * @author zyw
     * @return 返回
     */
    def getJsapiTicket(){
        if(redisService.exists("wechat_api_ticket")){
            return redisService.get("wechat_api_ticket");
        }else{
            String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi"
            try {
                url  = url.replace("ACCESS_TOKEN", URLEncoder.encode(getAccessToken(),"UTF-8"))
                def entity = httpService.postJson(url).entity
                if(entity.errcode == 0){
                    String ticket = entity.ticket
                    redisService.setex("wechat_api_ticket", 600,ticket)
                    return ticket
                }else{
                    throw new MyException(441, "wechat api ticket get error", "参数错误")
                }
            } catch (UnsupportedEncodingException e2) {
                throw new MyException(440, "params is error", "参数错误")
            }
        }
    }

    /**
     * 微信生成二维码
     *
     * @param params
     *
     * 官方文档链接 https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1443433542
     * URL: https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN
     *
     * 临时二维码请求说明
     * {"expire_seconds": 604800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
     * {"expire_seconds": 604800, "action_name": "QR_STR_SCENE", "action_info": {"scene": {"scene_str": "test"}}}
     *
     * 永久二维码请求说明
     * {"action_name": "QR_LIMIT_SCENE", "action_info": {"scene": {"scene_id": 123}}}
     * {"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "test"}}}
     *
     * expire_seconds	该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     * action_name	二维码类型，QR_SCENE为临时的整型参数值，QR_STR_SCENE为临时的字符串参数值，QR_LIMIT_SCENE为永久的整型参数值，QR_LIMIT_STR_SCENE为永久的字符串参数值
     * action_info	二维码详细信息
     * scene_id	场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
     * scene_str	场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
     *
     * @return {"ticket":"gQH47joAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2taZ2Z3TVRtNzJXV1Brb3ZhYmJJAAIEZ23sUwMEmm3sUw==","expire_seconds":60,"url":"http://weixin.qq.com/q/kZgfwMTm72WWPkovabbI"}
     * ticket	获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     * expire_seconds	该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）。
     * url	二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    def getQrcode(def params){
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN"
        try {
            if(!params){
                params = [
                        "expire_seconds": 3 * 24 * 60 * 60,
                        "action_name": "QR_STR_SCENE",
                        "action_info": [
                                "scene": [
                                        "scene_str": "http://healthtoo.prospect-media.cn/ht-wx"
                                ]
                        ]
                ]
            }
            url  = url.replace("ACCESS_TOKEN", URLEncoder.encode(getAccessToken(),"UTF-8"))
            return httpService.postJson(url, JsonUtils.objToJsonStr(params)).entity
        } catch (Exception e) {
            throw new MyException(440, e.message, e.message)
        }
    }

    /**
     * 根据url生成jsapi使用的签名等信息
     * @param url
     * @return
     */
    def getJsapiSign(String url){
        if(Environment.isDevelopmentMode()) return [success: false]
        String jsapi_ticket = getJsapiTicket()
        if(jsapi_ticket){
            long timestamp = System.currentTimeMillis()/1000
            String nonceStr = createUUID();
            url = url.replace("*AND*", "&")
            String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url
            MessageDigest crypt = MessageDigest.getInstance("SHA-1")
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"))
            Formatter formatter = new Formatter()
            for (byte b : crypt.digest()) {
                formatter.format("%02x", b)
            }
            String signature = formatter.toString()
            formatter.close()
            def rep = [
                    timestamp:      timestamp,
                    nonceStr:       nonceStr,
                    signature:      signature,
                    appId:          APP_ID,
                    debug:          false,
                    jsApiList:      ["checkJsApi","onMenuShareTimeline","onMenuShareAppMessage","onMenuShareQQ","onMenuShareWeibo","onMenuShareQZone"]
            ]
            return rep
        }else{
            throw new MyException(540, "jsapi_ticket is error", "服务器错误")
        }
    }

    /**	------------------------------------------------------------------------------------------------------------------------
     * 获取用户信息
     * @author zyw
     * @param open_id				获取的code
     * @param {"openid":"oyepit9Q0xlK4HhaY2_ETYguIphA","nickname":"朱亚伟","sex":1,"language":"zh_CN","city":"","province":"","country":"中国","headimgurl":"http:\/\/wx.qlogo.cn\/mmopen\/yMqvXTzibrfJ8FC1MVyIDuy7xMZQdWN689FUSvxJT9CAmCPBicVUjBFexJdqUvMwFh1zRpaYF56Wn17ZAKw8h0Vc5aeOp2icaib7\/0","privilege":[]}
     */
    def getUserInfo(String open_id,String access_token) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        try {
            url = url.replace("ACCESS_TOKEN", URLEncoder.encode(access_token,"UTF-8"));
            url = url.replace("OPENID", URLEncoder.encode(open_id,"UTF-8")); //snsapi_userinfo  //snsapi_base
//            return httpService.postJson(url).entity
            StringBuffer bufferResaccessdetail = new StringBuffer();
            try {
                URL realUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                BufferedReader read = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String valueString = null;
                while ((valueString=read.readLine())!=null){
                    bufferResaccessdetail.append(valueString);
                }
                read.close();
                inputStream.close();
                inputStream = null;
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                return null
            }
            return JsonUtils.strJsonToJson(bufferResaccessdetail.toString())
        } catch (UnsupportedEncodingException e) {
            throw new MyException(440, "params is error", "参数错误")
        }
    }

    /**
     * 获取用户信息(根据openId)
     * @param open_id
     * @param access_token
     * @return
     */
    def getUserInfo(String openId) {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        try {
            url = url.replace("ACCESS_TOKEN", URLEncoder.encode(getAccessToken(),"UTF-8"));
            url = url.replace("OPENID", URLEncoder.encode(openId,"UTF-8")); //snsapi_userinfo  //snsapi_base
            return httpService.postJson(url).entity
        } catch (UnsupportedEncodingException e) {
            throw new MyException(440, "params is error", "参数错误")
        }
    }

    /**
     * 判断用户是不是已经关注
     * @param openId
     * @return
     */
    boolean isSubscribe(String openId){
        try {
            if(!openId) return false
            def userInfo = getUserInfo(openId)
            def subscribe = userInfo.subscribe
            return subscribe ? subscribe as boolean : false
        }catch (Exception e){
            return false
        }
    }


    /**	------------------------------------------------------------------------------------------------------------------------
     * 微信公共的请求工具类
     * @param requestUrl 		请求微信的url(以替换参数和转码)
     * @param param				请求微信携带的参数(实体类/map/xml需转为String)
     * @return					返回微信相应的数据
     * url参数替换和转码	requestUrl  = requestUrl.replace("CODE",URLEncoder.encode(code,"UTF-8"));
     * url参数中不需要替换appid和secret
     */
    String httpsRequestPost(String requestUrl,String param){
        try {
            requestUrl  = requestUrl.replace("APPID", URLEncoder.encode(APP_ID,"UTF-8"));
        } catch (UnsupportedEncodingException e) {}
        try {
            requestUrl = requestUrl.replace("SECRET", URLEncoder.encode(APP_SECRET,"UTF-8"));
        } catch (UnsupportedEncodingException e) {}
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Attr","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            if (null != param) {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(param.getBytes("UTF-8"));
                outputStream.close();
            }
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;

            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
        } catch (ConnectException ce) {
        } catch (Exception e) {
        }
        return buffer.toString();
    }

    /**	------------------------------------------------------------------------------------------------------------------------
     * 微信退款
     * @param param				请求微信携带的参数(实体类/map/xml需转为String)
     * @return					返回微信相应的数据
     */
    public  String httpsRequestRefund(String param){
        String requestUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund"
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(CRET_LOCAL_PATH));//加载本地的证书进行https加密传输
        try {
            //指定PKCS12的密码(商户ID)
            keyStore.load(instream, CERT_PASSWORD.toCharArray());//设置证书密码
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, CERT_PASSWORD.toCharArray()).build();
        String[] supportProtocals = new String[1]
        supportProtocals[0] = "TLSv1"
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, supportProtocals, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

        HttpPost httpPost = new HttpPost(requestUrl);
        //将要提交给API的数据对象转换成XML格式数据Post给API
        httpPost.addHeader("Content-Attr", "text/xml");
        httpPost.setEntity(new StringEntity(param, "UTF-8"));
        String result=null;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            httpPost.abort();
        } catch (Exception e) {
            log.error("httpClient execute exception:", e)
        }
        return result;
    }

    /**
     * 企业付款(不实名认证)
     * @param openid           openid
     * @param partnerTradeNo   商户订单号，需保持唯一性
     * @param amount           金额(分)
     * @param desc             备注
     * @return
     */
    boolean httpsEnterprisePay(String openid, String partnerTradeNo, Integer amount){
        return httpsEnterprisePay(openid, partnerTradeNo, "NO_CHECK" ,null, amount, "提现微信")
    }
    /**
     * 企业付款(不实名认证)
     * @param openid           openid
     * @param partnerTradeNo   商户订单号，需保持唯一性
     * @param amount           金额(分)
     * @param desc             备注
     * @return
     */
    boolean httpsEnterprisePay(String openid, String partnerTradeNo, Integer amount, String desc){
        return httpsEnterprisePay(openid, partnerTradeNo, "NO_CHECK" ,null, amount, desc)
    }

    /**
     * 企业付款(强制实名认证)
     * @param openid
     * @param partnerTradeNo
     * @param reUserName
     * @param amount
     * @param desc
     * @return
     */
    public boolean httpsEnterprisePay(String openid, String partnerTradeNo, String reUserName, Integer amount, String desc){
        return httpsEnterprisePay(openid, partnerTradeNo, "OPTION_CHECK" ,reUserName, amount, desc)
    }




    /**
     * 带证书的请求类
     * @param url       请求的url
     * @param param     请求的参数
     * @return
     */
    def httpSSL(String url, def param){
        if(url && param){
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File(CRET_LOCAL_PATH));//加载本地的证书进行https加密传输
            try {
                //指定PKCS12的密码(商户ID)
                keyStore.load(instream, CERT_PASSWORD.toCharArray());//设置证书密码
                SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, CERT_PASSWORD.toCharArray()).build();
                String[] supportProtocals = new String[1]
                supportProtocals[0] = "TLSv1"
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, supportProtocals, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
                CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

                HttpPost httpPost = new HttpPost(url);
                //将要提交给API的数据对象转换成XML格式数据Post给API
                httpPost.addHeader("Content-Attr", "text/xml");
                httpPost.setEntity(new StringEntity(mapToXmlStr(param), "UTF-8"));
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    httpPost.abort();
                    //解析xml成map
                    if(entity){
                        return xmlStrToMap(EntityUtils.toString(entity, "UTF-8"))
                    }else{
                        return null;
                    }
                } catch (Exception e) {
                    log.error("httpClient execute exception3: " + e.getMessage())
                    return null;
                }
            } catch (CertificateException e) {
                log.error("keyStore.load execute exception2: " + e.getMessage())
                return null;
            } catch (NoSuchAlgorithmException e) {
                log.error("keyStore.load execute exception1: " + e.getMessage())
                return null;
            } finally {
                instream.close();
            }
        }else{
            return null
        }
    }

    boolean checkSignature(String signature, String timestamp, String nonce, String echostr, String token, HttpServletResponse response){
        Map<String, Object> map = new TreeMap<String, Object>()
        map.put("timestamp", timestamp)
        map.put("nonce",nonce)
        map.put("token", token)
        String sign = getSign(map,"UTF-8",KEY)
        if(signature.equals(sign)){
            payNotice(response, true)
            return true
        }else{
            return false
        }
    }

    /**
     * 分转元
     * @param i
     * @return
     */
    public  double feeIntToDouble(String i){
        return (double)Integer.valueOf(i)/100;
    }

    /**
     * 元转分
     * @param d
     * @return
     */
    public  String feeDoubleToInt(String d){
        return (int) ((double)Double.valueOf(d)*100)+"";
    }

    /**	------------------------------------------------------------------------------------------------------------------------
     * 获取uuid的方法
     * @return
     */
    public  String createUUID(){
        return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
    }

    /**	------------------------------------------------------------------------------------------------------------------------
     * 签名算法
     * @param o 要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */

    String getSign(Map<String,Object> map,String charsetname,String key){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry : map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        if(key==null){
            result=result.substring(0, result.length()-1);
        }else{
            result += "key=" + key;
        }
        result = MD5Utils.MD5Encode(result,charsetname).toUpperCase();
        return result;
    }

    public  String mapToUrlStr(Map<String,Object> map){
        String str="";
        for(Map.Entry<String,Object> entry : map.entrySet()){
            if(entry.getValue()!=""){
                try {
                    str+=entry.getKey() + "=" + URLEncoder.encode((String)entry.getValue(),"GBK") + "&";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return str.substring(0, str.length()-1);
    }

    /**
     * xmlStr转map
     * @param xmlStr
     * @return
     * @throws Exception
     */
    def xmlStrToMap(String xmlStr) {
        return new XmlParser().parseText(xmlStr)
    }

    /**	------------------------------------------------------------------------------------------------------------------------
     * map转xmlStr
     * @param dataMap
     * @return
     *
     */
    public  String mapToXmlStr(Map<String, Object> dataMap) {
        StringBuilder strBuilder = new StringBuilder();
//		strBuilder.append("<?xml version='1.0' encoding='UTF-8' ?>");
        strBuilder.append("<xml>");
        Set<String> objSet = dataMap.keySet();
        for (Object key : objSet) {
            if (key == null) {
                continue;
            }
//            strBuilder.append("\n");
            strBuilder.append("<").append(key.toString()).append(">");
            Object value = dataMap.get(key);
            strBuilder.append(coverter(value).trim());
            strBuilder.append("</").append(key.toString()).append(">");
        }
        strBuilder.append("</xml>");
//        strBuilder.append("\n</xml>");
        return strBuilder.toString();
    }
    public  String coverter(Object[] objects) {
        StringBuilder strBuilder = new StringBuilder();
        for (Object obj : objects) {
            strBuilder.append("<item className=").append(obj.getClass().getName()).append(">\n");
            strBuilder.append(coverter(obj));
            strBuilder.append("</item>\n");
        }
        return strBuilder.toString();
    }
    public  String coverter(Collection<?> objects) {
        StringBuilder strBuilder = new StringBuilder();
        for (Object obj : objects) {
            strBuilder.append("<item className=")
                    .append(obj.getClass().getName()).append(">\n");
            strBuilder.append(coverter(obj));
            strBuilder.append("</item>\n");
        }
        return strBuilder.toString();
    }
    public  String coverter(Object object) {
        if (object instanceof Object[]) {
            return coverter((Object[]) object);
        }
        if (object instanceof Collection) {
            return coverter((Collection<?>) object);
        }
        StringBuilder strBuilder = new StringBuilder();
        if (isObject(object)) {
            Class<? extends Object> clz = object.getClass();
            Field[] fields = clz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = null;
                try {
                    value = field.get(object);
                } catch (IllegalArgumentException e) {
                    continue;
                } catch (IllegalAccessException e) {
                    continue;
                }
                strBuilder.append("<").append(fieldName).append(" className=\"").append(value.getClass().getName()).append("\">\n");
                if (isObject(value)) {
                    strBuilder.append(coverter(value));
                } else {
                    strBuilder.append(value.toString() + "\n");
                }
                strBuilder.append("</").append(fieldName).append(">\n");
            }
        } else if (object == null) {
            strBuilder.append("null\n");
        } else {
            strBuilder.append(object.toString() + "\n");
        }
        return strBuilder.toString();
    }
    private  boolean isObject(Object obj) {
        if (obj == null) {return false;}
        if (obj instanceof String) {return false;}
        if (obj instanceof Integer) {return false;}
        if (obj instanceof Double) {return false;}
        if (obj instanceof Float) {return false;}
        if (obj instanceof Byte) {return false;}
        if (obj instanceof Long) {return false;}
        if (obj instanceof Character) {return false;}
        if (obj instanceof Short) {return false;}
        if (obj instanceof Boolean) {return false;}
        return true;
    }

    /**
     * 回调通知
     * 告诉微信服务器，我收到信息了，不要在调用回调url了
     */
    void payNotice(HttpServletResponse response, String resXml) {
        BufferedOutputStream out = null
        try {
            out = new BufferedOutputStream(response.getOutputStream())
            out.write(resXml.getBytes())
        } catch (IOException e) {
            e.printStackTrace()
        } finally {
            try {
                out.flush()
                out.close()
            } catch (IOException e) {
                e.printStackTrace()
            }
        }
    }

    String getNotityXml(HttpServletRequest request) {
        String inputLine = "";
        String notityXml = "";
        try {
            while ((inputLine = request.getReader().readLine()) != null) {
                notityXml += inputLine;
            }
            request.getReader().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notityXml;
    }

    /** ------------------------------------------------------------------------------------------------------------------------
     * 发送模版消息	sendTemplateMsg
     * @param openId            openid
     * @param templateId        消息模版id
     * @param topcolor          title 颜色
     * @param data              消息模版        "data":{"title":{"value":"测试模版消息","color":"#173177"},"name":{"value":"朱亚伟","color":"#173177"},"age":{"value":"27","color":"#173177"},"sex":{"value":"男","color":"#173177"}}
     * @return
     */
    boolean sendTemplateMsg(String openId, String templateId, def data) {
        sendTemplateMsg(openId, templateId, null, data)
    }

    /** ------------------------------------------------------------------------------------------------------------------------
     * 发送模版消息	sendTemplateMsg
     * @param openId            openid
     * @param templateId        消息模版id
     * @param url               点击模板消息跳转的url
     * @param data              消息模版 "data":{"title":{"value":"测试模版消息","color":"#173177"},"name":{"value":"朱亚伟","color":"#173177"},"age":{"value":"27","color":"#173177"},"sex":{"value":"男","color":"#173177"}}
     * @return
     */
    boolean sendTemplateMsg(String openId,String templateId, String url, def data) {
        println( url )
//        String httpUrl = "https://api.weixin.qq.com/cgi-bin/message/template/producer?access_token=ACCESS_TOKEN"      //之前的版本url
        String httpUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN"
        String accessToken = getAccessToken()
        if(accessToken){
            try {
                httpUrl = httpUrl.replace("ACCESS_TOKEN", URLEncoder.encode(accessToken, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage())
            }
            def dataObj = [touser:openId,template_id:templateId,url:url,data:data]
            def retJson = httpsRequestPost(httpUrl, JsonUtils.objToJsonStr(dataObj));
            def jsonObj = JsonUtils.strJsonToJson(retJson)
            if(jsonObj.errcode == 0 && jsonObj.errmsg.equals("ok")){
                return true
            }else{
                log.error(retJson)
                return false
            }
        }else{
            return false
        }
    }

    boolean sendTemplateMsgTest(String accessToken, String openId,String templateId) {
        String httpUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN"
        if(accessToken){
            try {
                httpUrl = httpUrl.replace("ACCESS_TOKEN", URLEncoder.encode(accessToken, "UTF-8"))
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage())
            }
            def data = [
                    "first": ["value": "first", "color": "#173177"],
                    "keyword1" : ["value": "keyword1", "color": "#173177"],
                    "keyword2"  : ["value": "keyword2", "color": "#173177"],
                    "keyword3"  : ["value": "keyword3", "color": "#173177"],
                    "remark"  : ["value": "备注", "color": "#173177"]
            ];
            def dataObj = [touser:openId, template_id:templateId, data:data]
            def retJson = httpsRequestPost(httpUrl, JsonUtils.objToJsonStr(dataObj))
            def jsonObj = JsonUtils.strJsonToJson(retJson)
            if(jsonObj.errcode == 0 && jsonObj.errmsg.equals("ok")){
                return true
            }else{
                println retJson
                return false
            }
        }else{
            return false
        }
    }


    /**	------------------------------------------------------------------------------------------------------------------------
     * 统一下单参数封装
     * @param totalFee(分)
     * @param body
     * @param openId
     * @param tradeType JSAPI，NATIVE，APP
     * @return
     */
    def unifiedorder(String outTradeNo,Integer totalFee,String body,String openId,String tradeType){
        Map<String, Object> map = new TreeMap<String, Object>()
        map.put("appid",APP_ID)							                                    //微信分配的公众账号ID（企业号corpid即为此appId）
        map.put("mch_id", MCH_ID)							                                //微信支付分配的商户号
        map.put("nonce_str",createUUID())										            //随机字符串，不长于32位。推荐随机数生成算法
        map.put("body", body)													            //商品或支付单简要描述
        map.put("out_trade_no",outTradeNo)										            //商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
        map.put("openid",openId)
        map.put("total_fee", totalFee)	                                                    //订单总金额，单位为分，详见支付金额
        map.put("spbill_create_ip", IP)					                                    //APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
        map.put("notify_url",NOTIFY_URL)	                                                //接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
        map.put("trade_type",tradeType)										                //取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
        String sign = getSign(map,"UTF-8",KEY)
        map.put("sign", sign)													            //签名，详见签名生成算法
        return map
    }

    /**	------------------------------------------------------------------------------------------------------------------------
     * jsAPI支付
     * @param prepayId	预支付id
     * @return
     */
    def jsAPIOrder(String prepayId){
        Map<String, Object> map =  new TreeMap<String, Object>();
        map.put("appId", APP_ID);
        map.put("timeStamp",(System.currentTimeMillis() / 1000)+"");
        map.put("nonceStr",createUUID());
        map.put("package","prepay_id="+prepayId);
        map.put("signType","MD5");
        String sign = getSign(map,"UTF-8",KEY);
        map.put("paySign",sign);
        map.put("debug", true);
        return map;
    }

    /**
     * 退款
     * @param outOradeNo
     * @param totalFee
     * @param refundFee
     * @return
     */
    def refundOrder(String outOradeNo,Integer totalFee,Integer refundFee){
        Map<String, Object> map =  new TreeMap<String, Object>();
        map.put("appid", APP_ID);
        map.put("mch_id",MCH_ID);
        map.put("nonce_str",createUUID());
        map.put("out_trade_no",outOradeNo);
        map.put("out_refund_no",createUUID());
        map.put("total_fee",totalFee);
        map.put("refund_fee",refundFee);
        map.put("op_user_id",MCH_ID);
        String sign = getSign(map,"UTF-8",KEY);
        map.put("sign",sign);
        return map;
    }

    /**
     * 查询订单/查询退款
     * @param outTradeNo
     * @return
     */
    def orderqueryByOutTradeNo(String outTradeNo){
        Map<String, Object> map =  new TreeMap<String, Object>();
        map.put("appid", APP_ID);
        map.put("mch_id",MCH_ID);
        map.put("nonce_str",createUUID());
        map.put("out_trade_no",outTradeNo);
        String sign = getSign(map,"UTF-8",KEY);
        map.put("sign",sign);
        return map;
    }

    /**
     * 企业支付
     * @param openid
     * @param partnerTradeNo
     * @param reUserName
     * @param amount
     * @param desc
     * @return
     */
    def enterprisePay(String openid,String partnerTradeNo,String checkName, String reUserName,int amount,String desc){
        Map<String, Object> map =  new TreeMap<String, Object>();
        map.put("mch_appid", APP_ID)       //微信分配的公众账号ID（企业号corpid即为此appId）
        map.put("mchid",MCH_ID)            //微信支付分配的商户号
        map.put("nonce_str",createUUID())                      //随机字符串，不长于32位
        map.put("partner_trade_no",partnerTradeNo)             //商户订单号，需保持唯一性
        map.put("openid",openid)                              //商户appid下，某用户的openid
        map.put("check_name",checkName)                        //NO_CHECK：不校验真实姓名  FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账） OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
        map.put("re_user_name",reUserName)                     //收款用户真实姓名。   如果check_name设置为FORCE_CHECK或OPTION_CHECK，则必填用户真实姓名
        map.put("amount",amount)                               //企业付款金额，单位为分
        map.put("desc",desc)                                   //企业付款操作说明信息。必填。
        map.put("spbill_create_ip",IP)    //调用接口的机器Ip地址
        String sign = getSign(map,"UTF-8",KEY)
        map.put("sign",sign)
        return map
    }

    /**
     * 企业发红包
     * @param name      红包发送者名称
     * @param openid    接受红包的用户
     * @param amount    付款金额，单位分
     * @param wishing   红包祝福语
     * @param actName   活动名称
     * @param remark    备注信息
     * @return
     */
    def redpack(String name, String openid, Integer amount, String wishing, String actName, String remark){
        Map<String, Object> map =  new TreeMap<String, Object>();
        map.put("nonce_str",createUUID());                                          //随机字符串，不长于32位
        map.put("mch_billno", MCH_ID + yymmdd + getCount(yymmdd));  //商户订单号（每个订单号必须唯一）组成：mch_id+yyyymmdd+10位一天内不能重复的数字,接口根据商户订单号支持重入，如出现超时可再调用。
        map.put("mch_id",MCH_ID);                                   //微信支付分配的商户号
        map.put("wxappid",APP_ID);                                  //微信分配的公众账号ID
        map.put("send_name",name);                                                  //红包发送者名称
        map.put("re_openid",openid);                                                //接受红包的用户:用户在wxappid下的openid，服务商模式下可填入msgappid下的openid。
        map.put("total_amount",amount);                                             //付款金额，单位分
        map.put("total_num",1);                                                     //红包发放总人数 total_num=1
        map.put("wishing",wishing);                                                 //红包祝福语
        map.put("client_ip",IP);                                    //调用接口的机器Ip地址
        map.put("act_name",actName);                                                //活动名称
        map.put("remark",remark);                                                   //备注信息
        map.put("scene_id",SCENE_ID);                               //发放红包使用场景，红包金额大于200时必传  PRODUCT_1:商品促销 PRODUCT_2:抽奖 PRODUCT_3:虚拟物品兑奖 PRODUCT_4:企业内部福利  PRODUCT_5:渠道分润 PRODUCT_6:保险回馈 PRODUCT_7:彩票派奖 PRODUCT_8:税务刮奖
        String sign = getSign(map,"UTF-8",KEY);
        map.put("sign",sign);
        return map;
    }

    private String yymmdd = new Date().format("yyyyMMddHHmmss")
    private int count = 0
    private String ymd= ""
    def getCount(String yymmdd){
        String num
        if(ymd == yymmdd){
            count++
        }else{
            ymd = yymmdd
            count = 0
        }
        num = count + ""
        switch (num.length()){
            case 1 : num = "000" + num; break;
            case 2 : num = "00" + num; break;
            case 3 : num = "0" + num; break;
        }
        return num
    }


}
