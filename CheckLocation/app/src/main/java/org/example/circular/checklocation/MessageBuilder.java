package org.example.circular.checklocation;

/**
 * Created by alberto on 09/12/2016.
 */

public class MessageBuilder {


        String Build(String name, double x, double y){
            String loc;
            loc = String.format("#N%s*#X%f*#Y%f*",name, x, y );
            return loc;
        }


}
