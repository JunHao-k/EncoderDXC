import java.util.*;
import java.io.*;
import java.util.Queue;
/*
    Time complexity: 
    1) Setup ==> O(N)
    2) Encode ==> O(N)
    3) Decode ==> O(N)
    
    Total time complexity ==> O(N)
*/
class Transcoder {
    public char[] refTable;
    public HashMap<Character, Character> encodeMap = new HashMap<Character, Character>();
    public HashMap<Character, Character> decodeMap = new HashMap<Character, Character>();
    public char code;

    // Overloaded constructor
    public Transcoder(char code) {
        this.refTable = initRefTable();
        this.code = code;
        this.setup();
    }

    // Create the reference table
    public char[] initRefTable() {
        int count = 0;
        int count2 = 0;
        char[] refTableArr = new char[44];
        for (int i = 0; i < 26; i++) {
            refTableArr[i] = (char) (65 + i);
        }
        for (int i = 26; i < 36; i++) {
            refTableArr[i] = (char) (48 + count);
            count++;
        }
        for (int i = 36; i < 44; i++) {
            refTableArr[i] = (char) (40 + count2);
            count2++;
        }
        return refTableArr;
    }

    public void setRefTable() {
        this.refTable = initRefTable();
    }

    public char[] getRefTable() {
        return refTable;
    }

    public void setCode(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    // Create the encode hashmap and it's corresponding decode hashmap based on the offset character
    public void setup(){
        Queue<Character> tempQueue = new LinkedList<>();
        int refIndex = 0;
        int offsetValue = 0;
        int startIndex = 0;
        for(int i = 0; i < refTable.length; i++){
            if(refTable[i] == this.getCode()){
                refIndex = i;
                break;
            }
        }
        offsetValue = refIndex - 1;
        startIndex = 43 - offsetValue;
        for(int i = startIndex; i <= 43; i++){
            tempQueue.add(refTable[i]);
        }
        for(int i = 0; i <= startIndex - 1; i++){
            tempQueue.add(refTable[i]);
        }
        for(int i = 0; i < refTable.length; i++){
            encodeMap.put(refTable[i] , tempQueue.poll());
        }
        for(Map.Entry<Character , Character> eachEntry : encodeMap.entrySet()){
            this.decodeMap.put(eachEntry.getValue() , eachEntry.getKey());
        }
    }

    // Each character in plaintext = key of encode hashmap
    // Encode plaintext by accessing the corresponding encoded character via it's key
    // Use string builder to append each encoded character together to form final encoded string
    public String encode(String plainText) {
        StringBuilder sb = new StringBuilder(); 
        if (encodeMap == null) {
            this.setup();
        }
        sb.append(code);
        for (int i = 0; i < plainText.length(); i++) {
            if (encodeMap.containsKey(plainText.charAt(i))) {
                sb.append(encodeMap.get(plainText.charAt(i)));
            } 
            else{
                sb.append(plainText.charAt(i)); // Appending single characters is O(1)
            }
        }
        return sb.toString();
    }

    // Each character in encoded text = key of decode hashmap
    // Decode text by accessing the corresponding decoded character via it's key
    // Use string builder to append each decoded character together to form final plaintext
    public String decode(String encodedText) {
        StringBuilder sb = new StringBuilder();
        if (decodeMap == null) {
            this.setup();
        }
        for (int i = 1; i < encodedText.length(); i++) {
            if (decodeMap.containsKey(encodedText.charAt(i))) {
                sb.append(decodeMap.get(encodedText.charAt(i)));
            } 
            else{
                sb.append(encodedText.charAt(i));
            }
        }
        return sb.toString();
    }

}

public class Encoder{
    public static void main(String[] args) throws Exception {
        String encoded = "";
        String decoded = "";

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input a string for encoding = ");
        String inputStr = br.readLine().toUpperCase(); 

        System.out.println("Input encoding Offset Character = ");
        String inputCodeChar = br.readLine().toUpperCase();

        Transcoder transcoder = new Transcoder(inputCodeChar.charAt(0));

        encoded = transcoder.encode(inputStr);
        decoded = transcoder.decode(encoded);
        System.out.println("This is encoded answer ==> " + encoded);
        System.out.println("This is decoded answer ==> " + decoded);

    }
}
