package test;

public class Test {
    public static void main(String[] args) {
        String str = null;
        String input = "{\n" +
                "        \"p_result\": \"ok\", \n" +
                "        \"p_item\": [\n" +
                "         {\n" +
                "            \"p_id\": 132,\n" +
                "            \"p_name\": \"Николай\"\n" +
                "         }, \n" +
                "         {\n" +
                "            \"p_id\": 133,\n" +
                "            \"p_name\": \"Светлана\"\n" +
                "         }\n" +
                "    ]\n" +
                " }";

        String input2 = "{ " +
                "\"firstName\": \"YYY\", " +
                "\"lastName\": \"zzz\", " +
                "\"compId\": \"12345\", " +
                "}";
        System.out.println(input2);
        String st = null;
        if (st.equals(null)) System.out.println("null");
        else System.out.println("not null");
    }
}
