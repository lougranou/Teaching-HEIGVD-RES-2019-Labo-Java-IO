package ch.heigvd.res.labio.impl;

import java.util.logging.Logger;

/**
 * @author Olivier Liechti
 */
public class Utils {

    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    private static final char
            BREAK_LINE = '\n',
            CARRIAGE_RETURN = '\r',
            TAB = '\t';

    /**
     * This method looks for the next new line separators (\r, \n, \r\n) to extract
     * the next line in the string passed in arguments.
     *
     * @param lines a string that may contain 0, 1 or more lines
     * @return an array with 2 elements; the first element is the next line with
     * the line separator, the second element is the remaining text. If the argument does not
     * contain any line separator, then the first element is an empty string.
     */
    public static String[] getNextLine(String lines) {
        int i = 0;

        /**
         * searching next Break line or carriage return index
         */
        while (i < lines.length()) {
            char c = lines.charAt(i);

            if (c == BREAK_LINE) {
                /**
                 *  increment i because the end index is exclusive in sub-string
                 */
                ++i;
                break;

                /**
                 * check if c is not follow by a BREAK_LINE (windows case)
                 */
            } else if (c == CARRIAGE_RETURN) {
                if ((i + 1 < lines.length() && lines.charAt(i + 1) != BREAK_LINE)
                        || i == lines.length() - 1) {
                    /**
                     *  increment i because the end index is exclusive in sub-string
                     */
                    ++i;
                    break;
                }
            } else  if ( i == lines.length() -1){
                i = 0; // no return line char
                break;
            }

            ++i;
        }


        return new String[]{lines.substring(0, i), lines.substring(i)};
    }
}
