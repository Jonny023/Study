package com.jonny.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.transform.Transformer;
import org.jxls.transform.jexcel.JexcelTransformer;
import org.jxls.transform.poi.PoiTransformer;

/**
 * @author Jonny
 * @description url超链接
 * @date 2019/9/19
 */
public class LinkCommand extends AbstractCommand {

    private String url;     //url地址（必选）
    private String title;   //url显示标题（可选）
    private Area area;

    @Override
    public String getName() {
        return "merge";
    }

    @Override
    public Command addArea(Area area) {
        if (super.getAreaList().size() >= 1) {
            throw new IllegalArgumentException("You can add only a single area to 'link' command");
        }
        this.area = area;
        return super.addArea(area);
    }

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        if(StringUtils.isBlank(url)){
            throw new NullPointerException("url不能为空");
        }
        area.applyAt(cellRef, context);
        Transformer transformer = this.getTransformer();
        if(transformer instanceof PoiTransformer){
            poiLink(cellRef, context, (PoiTransformer)transformer);
        }else if(transformer instanceof JexcelTransformer){
            jxcelLink(cellRef, context, (JexcelTransformer)transformer);
        }
        return new Size(1, 1);
    }

    protected void poiLink(CellRef cellRef, Context context, PoiTransformer transformer){
        Object urlObj = getTransformationConfig().getExpressionEvaluator().evaluate(url, context.toMap());
        if(urlObj == null)
            return;

        String url = urlObj.toString();
        String title = url;
        if(StringUtils.isNotBlank(this.title)){
            Object titleObj = getTransformationConfig().getExpressionEvaluator().evaluate(this.title, context.toMap());
            if(titleObj != null)
                title = titleObj.toString();
        }

        Sheet sheet = transformer.getWorkbook().getSheet(cellRef.getSheetName());
        Row row = sheet.getRow(cellRef.getRow());
        if(row == null){
            row = sheet.createRow(cellRef.getRow());
        }
        Cell cell = row.getCell(cellRef.getCol());
        if(cell == null){
            cell = row.createCell(cellRef.getCol());
        }
//        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("HYPERLINK(\"" + url+ "\",\"" + title + "\")");
        if(!url.equals(title)){
            cell.setCellValue(title);
        }

        CellStyle linkStyle = cell.getCellStyle();
        Font cellFont= transformer.getWorkbook().createFont();
        cellFont.setUnderline((byte) 1);
//        cellFont.setColor(HSSFColor.BLUE.index);
        cellFont.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        linkStyle.setFont(cellFont);
    }

    protected void jxcelLink(CellRef cellRef, Context context, JexcelTransformer transformer){
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
