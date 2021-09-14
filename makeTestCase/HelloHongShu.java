package makeTestCase;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HelloHongShu {
    /**
     * 获取N个集合的笛卡尔积
     * <p/>
     * 说明：假如传入的字符串为："1,2,3==5,6==7,8"
     * 转换成字符串数组为：[[1, 2, 3], [5, 6], [7, 8]]
     * a=[1, 2, 3]
     * b=[5, 6]
     * c=[7, 8]
     * 其大小分别为：a_length=3，b_length=2，c_length=2，
     * 目标list的总大小为：totalSize=3*2*2 = 12
     * 对每个子集a，b，c，进行循环次数=总记录数/(元素个数*后续集合的笛卡尔积个数)
     * 对a中的每个元素循环次数=总记录数/(元素个数*后续集合的笛卡尔积个数)=12/(3*4)=1次，每个元素每次循环打印次数：后续集合的笛卡尔积个数=2*2个
     * 对b中的每个元素循环次数=总记录数/(元素个数*后续集合的笛卡尔积个数)=12/(2*2)=3次，每个元素每次循环打印次数：后续集合的笛卡尔积个数=2个
     * 对c中的每个元素循环次数=总记录数/(元素个数*后续集合的笛卡尔积个数)=12/(2*1)=6次，每个元素每次循环打印次数：后续集合的笛卡尔积个数=1个
     * <p/>
     * 运行结果：
     * [[1, 2, 3], [5, 6], [7, 8]]
     * 1,5,7,
     * 1,5,8,
     * 1,6,7,
     * 1,6,8,
     * 2,5,7,
     * 2,5,8,
     * 2,6,7,
     * 2,6,8,
     * 3,5,7,
     * 3,5,8,
     * 3,6,7,
     * 3,6,8]
     * <p/>
     * 从结果中可以看到：
     * a中的每个元素每个元素循环1次，每次打印4个
     * b中的每个元素每个元素循环3次，每次打印2个
     * c中的每个元素每个元素循环6次，每次打印1个
     *
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        String str = "a,b,c==d,e,f==x,y,z";
        String str = "x,y==x,y";
        Set<String> result = getSet(str);
//        System.out.println("set个数="+result.size());
        Iterator it=result.iterator();
        while(it.hasNext()){
        	System.out.println(it.next());
        }

    }
    public static Set<String> getSet(String str){
    	 List<String> result = descartes(str);
//    	 System.out.println("list个数="+result.size());
         Set<String> set=new HashSet<String>();         
         set.addAll(result);//给set填充         
//         result.clear();//清空list，不然下次把set元素加入此list的时候是在原来的基础上追加元素的
         return set;
    }

    @SuppressWarnings("rawtypes")
    public static List<String> descartes(String str) {
        String[] list = str.split("==");
        List<List> strs = new ArrayList<List>();
        for (int i = 0; i < list.length; i++) {
            strs.add(Arrays.asList(list[i].split(",")));
        }
//        System.out.println(strs);
        int total = 1;
        for (int i = 0; i < strs.size(); i++) {
            total *= strs.get(i).size();
        }
        String[] mysesult = new String[total];
        int now = 1;
        //每个元素每次循环打印个数
        int itemLoopNum = 1;
        //每个元素循环的总次数
        int loopPerItem = 1;
        for (int i = 0; i < strs.size(); i++) {
            List temp = strs.get(i);
            now = now * temp.size();
            //目标数组的索引值
            int index = 0;
            int currentSize = temp.size();
            itemLoopNum = total / now;
            loopPerItem = total / (itemLoopNum * currentSize);
            int myindex = 0;
            for (int j = 0; j < temp.size(); j++) {

                //每个元素循环的总次数
                for (int k = 0; k < loopPerItem; k++) {
                    if (myindex == temp.size())
                        myindex = 0;
                    //每个元素每次循环打印个数
                    for (int m = 0; m < itemLoopNum; m++) {
                        mysesult[index] = (mysesult[index] == null ? "" : mysesult[index] + ",").trim() + ((String) temp.get(myindex)).trim();
                        index++;
                    }
                    myindex++;

                }
            }
        }
        return Arrays.asList(mysesult);
    }

}
