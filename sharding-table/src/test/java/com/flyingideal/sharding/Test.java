package com.flyingideal.sharding;

import java.text.DecimalFormat;

/**
 * @author yanchao
 * @date 2019-11-15 10:55
 */
public class Test {

    public static String getPercent(Integer x,Integer total){
        if(x == null ||  x <= 0){
            return "0.00%";
        }
        if(total == null || total <= 0){
            return "100.00%";
        }

        String result="";//接受百分比的值
        double x_double=x*1.0;
        double tempresult=x_double/total;
        //NumberFormat nf   =   NumberFormat.getPercentInstance();//注释掉的也是一种方法
        //nf.setMinimumFractionDigits( 2 );//保留到小数点后几位
        DecimalFormat df1 = new DecimalFormat("0.00%");    //##.00%   百分比格式，后面不足2位的用0补齐
        //result=nf.format(tempresult);
        result= df1.format(tempresult);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getPercent(19999, 20000));
    }
}
