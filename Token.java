public class Token {

    public final static int ERROR = 0;
    public final static int BOOL = 1;
    public final static int ELSE = 2;
    public final static int IF = 3;
    public final static int INT = 4;
    public final static int RETURN = 5;
    public final static int VOID = 6;
    public final static int WHILE = 7;
    public final static int TRUE = 8;
    public final static int FALSE = 9;
    public final static int PLUS = 10;
    public final static int MINUS = 11;
    public final static int TIMES = 12;
    public final static int OVER = 13;
    public final static int LT = 14;
    public final static int LEQT = 15;
    public final static int GT = 16;
    public final static int GEQT = 17;
    public final static int EQ = 18;
    public final static int NEQ = 19;
    public final static int UBW = 20;
    public final static int OR = 21;
    public final static int AND = 22;
    public final static int ASSIGN = 23;
    public final static int SEMI = 24;
    public final static int COMMA = 25;
    public final static int LPAREN = 26;
    public final static int RPAREN = 27;
    public final static int LBRACK = 28;
    public final static int RBRACK = 29;
    public final static int LBRACE = 30;
    public final static int RBRACE = 31;
    public final static int ID = 32;
    public final static int NUM = 33;
    
    public int m_type;
    public String m_value;
    public int m_line;
    public int m_column;
    
    Token(int type, String value, int line, int column) {
        m_type = type;
        m_value = value;
        m_line = line;
        m_column = column;
    }
    
    public String toString() {
        switch (m_type) {
            case BOOL: return "BOOL";
            case ELSE: return "ELSE";
            case IF: return "IF";
            case INT: return "INT";
            case RETURN: return "RETURN";
            case VOID: return "VOID";
            case WHILE: return "WHILE";
            case TRUE: return "TRUE";
            case FALSE: return "FALSE";
            case PLUS: return "PLUS";
            case MINUS: return "MINUS";
            case TIMES: return "TIMES";
            case OVER: return "OVER";
            case LT: return "LT";
            case LEQT: return "LEQT";
            case GT: return "GT";
            case GEQT: return "GEQT";
            case EQ: return "EQ";
            case NEQ: return "NEQ";
            case UBW: return "UBW";
            case OR: return "OR";
            case AND: return "AND";
            case ASSIGN: return "ASSIGN";
            case SEMI: return "SEMI";
            case COMMA: return "COMMA";
            case LPAREN: return "LPAREN";
            case RPAREN: return "RPAREN";
            case LBRACK: return "LBRACK";
            case RBRACK: return "RBRACK";
            case LBRACE: return "LBRACE";
            case RBRACE: return "RBRACE";
            case ID: return "ID(" + m_value + ")";
            case NUM: return "NUM(" + m_value + ")";
            case ERROR: return "ERROR(" + m_value + ")";
            default: return "UNKNOWN(" + m_value + ")";
        }
    }
}