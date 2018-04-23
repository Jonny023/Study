```
loadAllDatas({curr: 1});
function loadAllDatas(c) {
    var index = layer.load(1);
    $.ajax({
        url: "/personalActivity/loadDatas",
        type: "POST",
        data: c,
        async: true,
        dataType: "json",
        success: function (res) {
            id = res.id;
            username = res.username;
            layer.close(index);
            var arr = [];
            $("#data-table tr").remove();
            if(res.personalActivity.length>0) {
                $.each(res.personalActivity,function (i,data) {
                    arr[i] = '<tr>'+
                        '<td>'+
                        '<input type="checkbox" name="checkbox_id" value="'+data.id+'" lay-skin="primary" />'+
                        '</td>'+
                        '<td>'+data.perName+'</td>'+
                        '<td>'+data.activityTheme+'</td>'+
                        '<td>'+data.activityType+'</td>'+
                        '<td>'+data.activityTime+'</td>'+
                        '<td>'+data.participants+'</td>'+
                        '<td>'+(data.audit=="2"?"已审核":(data.audit=="1"?"已推荐":(data.audit=="-1"?"未通过":"未推荐")))+'</td>'+
                    '<td>'+
                    (data.audit=="-1"||data.audit=="0"?'<a href="/mztj/personalActivity/edit?id='+data.id+'&combinerId=1" class="layui-btn layui-btn-mini layui-btn-sm"><i class="layui-icon">&#xe642;</i>编辑</a>':'')+
                    '<a href="/mztj/personalActivity/show?id='+data.id+'&combinerId=1" class="layui-btn layui-btn-mini layui-btn-sm"><i class="layui-icon">&#xe62a;</i>查看</a>'+
                    '</td>'+
                    '</tr>'
                });
            }else{
                arr[0] = '<tr><td style="color:red;text-align: center" colspan="8">暂无记录</td></tr>'
            }
            $("#data-table").append(arr.join('')).fadeIn(20);

            form.render();
            page(c,res.offset,res.total,res.totalPage,res.max);
        },
        error: function (xhr) {
            layer.close(index);
            layer.alert("网络连接失败！",{icon:2});
        },
        done: function () {

        }
    });
}

function  page(c,curr,total,totalPage,pageSize) {
    laypage.render({
        elem: "pagion1",
        count: total,
        limit: pageSize || 3,
        curr: curr || 1, //当前页
        groups: 5,
        jump: function(obj, first){ //触发分页后的回调
            if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
                c.curr = obj.curr;
                loadAllDatas(c);
            }
        }
    })
}
            
```
