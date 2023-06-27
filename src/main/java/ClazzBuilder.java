//import java.util.*;
//
//public class ClazzBuilder {
//
//
//
//
//    public static void main(String[] args) {
////        Signature signature = parseSignature("String getName(String subName)");
////        System.out.println(Arrays.toString(signature.getArgumentTypes()));
////        System.out.println(signature.getName());
////        System.out.println(signature.getDescriptor());
////        System.out.println(signature.getReturnType());
//    }
//
//    private static final Map<String ,String> transforms = new HashMap<>();
//
//    static {
//        transforms.put("void", "V");
//        transforms.put("byte", "B");
//        transforms.put("char", "C");
//        transforms.put("double", "D");
//        transforms.put("float", "F");
//        transforms.put("int", "I");
//        transforms.put("long", "J");
//        transforms.put("short", "S");
//        transforms.put("boolean", "Z");
//    }
//
//    public static Signature parseSignature(String s) {
//        int space = s.indexOf(' ');
//        int lparen = s.indexOf('(', space);
//        int rparen = s.indexOf(')', lparen);
//        String returnType = s.substring(0, space);
//        String methodName = s.substring(space + 1, lparen);
//        StringBuilder sb = new StringBuilder();
//        sb.append('(');
//        for (Iterator it = parseTypes(s, lparen + 1, rparen).iterator(); it.hasNext();) {
//            sb.append(it.next());
//        }
//        sb.append(')');
//        sb.append(map(returnType));
//        return new Signature(methodName, sb.toString());
//    }
//
//    public static TestPackage.Type parseType(String s) {
//        return TestPackage.Type.getType(map(s));
//    }
//
//    public static TestPackage.Type[] parseTypes(String s) {
//        List names = parseTypes(s, 0, s.length());
//        TestPackage.Type[] types = new TestPackage.Type[names.size()];
//        for (int i = 0; i < types.length; i++) {
//            types[i] = TestPackage.Type.getType((String)names.get(i));
//        }
//        return types;
//    }
//
//    private static List parseTypes(String s, int mark, int end) {
//        List types = new ArrayList(5);
//        for (;;) {
//            int next = s.indexOf(',', mark);
//            if (next < 0) {
//                break;
//            }
//            types.add(map(s.substring(mark, next).trim()));
//            mark = next + 1;
//        }
//        types.add(map(s.substring(mark, end).trim()));
//        return types;
//    }
//
//    private static String map(String type) {
//        if (type.equals("")) {
//            return type;
//        }
//        String t = (String)transforms.get(type);
//        if (t != null) {
//            return t;
//        } else if (type.indexOf('.') < 0) {
//            return map("java.lang." + type);
//        } else {
//            StringBuilder sb = new StringBuilder();
//            int index = 0;
//            while ((index = type.indexOf("[]", index) + 1) > 0) {
//                sb.append('[');
//            }
//            type = type.substring(0, type.length() - sb.length() * 2);
//            sb.append('L').append(type.replace('.', '/')).append(';');
//            return sb.toString();
//        }
//    }
//}
