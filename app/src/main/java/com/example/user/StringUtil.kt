package com.example.user

/**
 * Created by user on 16. 11. 19.
 */


class StringUtil{
    
    companion object{

        public fun StringReplace(str: String) : String {
            var tmp : String = str;
            
            if (tmp == null) return "12가3763";
            tmp = tmp.replace(" ", "")
            tmp = tmp.replace("십이", "12");
            tmp = tmp.replace("시비", "12");
            tmp = tmp.replace("집에", "11");
            tmp = tmp.replace("십일", "11");
            tmp = tmp.replace("굳이 배", "91");
            tmp = tmp.replace("시보", "15");

            return tmp;
        }
    }
}
