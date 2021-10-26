import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final ArrayList<String> strings = new ArrayList<>();
    private static int index;
    private static String sym;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str;
        while (scanner.hasNextLine()){
            str = scanner.nextLine();

            if (str.isEmpty())break;

            char[] ch = str.toCharArray();
            int len = str.length();
            Label:
            for (int i=0; i<len; i++){
                switch (ch[i]) {
                    case ' ':
                    case '\t':
                        continue;
                    case ';':
                        strings.add(";");
                        break;
                    case '(':
                        strings.add("(");
                        break;
                    case ')':
                        strings.add(")");
                        break;
                    case '{':
                        strings.add("{");
                        break;
                    case '}':
                        strings.add("}");
                        break;
                    case '/':
                        if (ch[i+1] == '/'){
                            break Label;
                        }else if (ch[i+1] == '*'){
                            i+=2;
                            for (;i<len-1||len==0;i++){
                                if (len!=0&&ch[i] == '*'){
                                    if (ch[i+1]=='/'){
                                        i++;
                                        continue Label;
                                    }else continue ;
                                }if (i == len - 2){
                                    if (!scanner.hasNextLine())System.exit(-1);
                                    str = scanner.nextLine();
                                    ch = str.toCharArray();
                                    len = str.length();
                                    i=-1;
                                }
                            }
                        }
                        break ;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(ch[i]);
//                        int digt = ch[i] - '0';
                        while (i < len-1 && (ch[i+1]>='0' && ch[i+1]<='9' || ch[i+1]>='a' && ch[i+1]<='f'
                                || ch[i+1]>='A' && ch[i+1]<='F' || ch[i+1]=='x'||ch[i+1]=='X')) {
                            i++;
                            stringBuilder.append(ch[i]);
//                            digt *= 10;
//                            digt += ch[i] - '0';
                        }
                        strings.add(stringBuilder.toString());
                        break;
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                    case 'g':
                    case 'h':
                    case 'i':
                    case 'j':
                    case 'k':
                    case 'l':
                    case 'm':
                    case 'n':
                    case 'o':
                    case 'p':
                    case 'q':
                    case 'r':
                    case 's':
                    case 't':
                    case 'u':
                    case 'v':
                    case 'w':
                    case 'x':
                    case 'y':
                    case 'z':
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'E':
                    case 'F':
                    case 'G':
                    case 'H':
                    case 'I':
                    case 'J':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'S':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    case 'Z':
                    case '_':
                        StringBuilder sb = new StringBuilder();
                        sb.append(ch[i]);
                        while (i<len-1&&((ch[i+1]>='0' && ch[i+1]<='9')||(ch[i+1]>='a' && ch[i+1]<='z')||(ch[i+1]>='A' && ch[i+1]<='Z')||ch[i+1]=='_')){
                            i++;
                            sb.append(ch[i]);
                        }
                        String string = sb.toString();
                        switch (string) {
                            case "int" -> strings.add("i32");
                            case "main" -> strings.add("@main");
                            case "return" -> strings.add("ret");
                            default -> System.exit(-1); //Error
                        }
                        break;
                    default:
                        System.exit(-1); //Error
                }
            }
        }
        index = 0;
        sym = nextString();
        if (compUnit()){
            System.out.println("define dso_local i32 @main(){\n" +
                    "    ret i32 " + strings.get(6) +
                    "\n}");
        }else System.exit(-1);//error
    }

    static String nextString(){
        if (index < strings.size())
            return strings.get(index++);
        else
            return null;
    }

    static boolean compUnit(){
        return funcDef();
    }

    static boolean funcDef(){
        if (!funcType())return false;
        if (!ident())return  false;
        if (sym.equals("(")){
            sym = nextString();
            if (sym != null){
                if (sym.equals(")")){
                    sym = nextString();
                    return block();
                }else return false;
            }else return false;
        }else return false;
    }

    static boolean funcType(){
        if (sym.equals("i32")){
            sym = nextString();
            return true;
        }else return false;
    }

    static boolean ident(){
        if (sym.equals("@main")){
            sym = nextString();
            return true;
        }return false;
    }

    static boolean block(){
        if (sym.equals("{")){
            sym = nextString();
            if (!stmt())return false;
            if (sym.equals("}")){
                sym = nextString();
                return sym == null;
            }else return false;
        }else return false;
    }

    static boolean stmt(){
        if (sym.equals("ret")){
            sym = nextString();
            if (!number())return false;
            if (sym.equals(";")){
                sym = nextString();
                return true;
            }else return false;
        }else return false;
    }

    static boolean number(){
        if (sym.length() == 1){
            char c = sym.charAt(0);
            if (c <= '9' && c >= '0'){
                sym = nextString();
                return true;
            }else return false;
        } else{
            char[] c = sym.toCharArray();
            int num=0;
            if (c[0] == '0' && (c[1]=='x'||c[1]=='X')){
                for (int i=2;i<sym.length();i++){
                    if (c[i]<='9'&&c[i]>='0' ||c[i]>='a'&&c[i]<='f'||c[i]>='A'&&c[i]<='F'){
                        num *= 16;
                        if (c[i]<='9'&&c[i]>='0')num += c[i] - '0';
                        else if (c[i]>='a'&&c[i]<='f')num += 10 + c[i] - 'a';
                        else num += 10 + c[i] - 'A';
                    }else return false;
                }
                strings.set(6,Integer.toString(num));
                sym = nextString();
                return true;
            }else if (c[0] == '0'){
                for (int i=1;i<sym.length();i++){
                    if (c[i]<='7'&&c[i]>='0'){
                        num *= 8;
                        num += c[i] -'0';
                    }else return false;
                }
                strings.set(6,Integer.toString(num));
                sym = nextString();
                return true;
            }else {
                for (int i=0;i<sym.length();i++){
                    if (c[i]<='9'&&c[i]>='0'){
                        num *= 10;
                        num += c[i] -'0';
                    }else return false;
                }
                strings.set(6,Integer.toString(num));
                sym = nextString();
                return true;
            }
        }
    }

}
