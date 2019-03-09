package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 * <p>
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

    private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

    private final char
            BREAK_LINE = '\n',
            CARRIAGE_RETURN = '\r',
            TAB = '\t';

    private Integer lineNumber;
    private boolean used;
    private String linePrefixe;

    /**
     * Constructor
     * @param out
     */
    public FileNumberingFilterWriter(Writer out) {
        super(out);
        this.lineNumber = 0; // first line is line one
        this.used = false;
        this.linePrefixe = new String();
    }

    @Override
    public void write(String str, int off, int len) throws IOException {

        boolean isBreakLine = false, isCarriageReturn = false;
        int previousIndex = 0;
        String tmp = str.substring(off, off + len);

        /**
         * if not already used write prefix and mark as used
         */
        if (!used) {
            writeLinePrefix();
            used = true;
        }


        for (int i = 0; i < tmp.length(); ++i) {

            /**
             * searching next Break line or carriage return index
             */
            while (i < tmp.length()) {
                char c = tmp.charAt(i);
                if (c == BREAK_LINE) {
                    isBreakLine = true;
                    break;

                    /**
                     * check if c is not follow by a BREAK_LINE (windows case)
                     */
                } else if (c == CARRIAGE_RETURN &&
                        (i + 1 < tmp.length() && tmp.charAt(i + 1) != BREAK_LINE)) {
                    isCarriageReturn = true;
                    break;
                }
                ++i;
            }

            /**
             * Case MacOS
             */
            if (isCarriageReturn) {
                super.write(tmp, previousIndex, i + 1 - previousIndex);
                previousIndex = i + 1;
                isCarriageReturn = false;
                writeLinePrefix();


                /**
                 *  Case Unix or Windows (they are treated in the same way)
                 */
            } else if (isBreakLine) {
                super.write(tmp, previousIndex, i + 1 - previousIndex);
                previousIndex = i + 1;
                isBreakLine = false;

                writeLinePrefix();
            }

            /**
             * no break line reached --> no line prefix needed, index is at the end of the string !
             */
            else {
                super.write(tmp, previousIndex, i - previousIndex);
                previousIndex = i + 1;
            }
        }
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        write(new String(cbuf), off, len + lineNumber * this.linePrefixe.length());
    }

    @Override
    public void write(int c) throws IOException {
        Character character = (char) c;
        write(character.toString(), 0, 1);
    }

    /**
     * Write the begining of the line : 'n° line' + '\t'
     * @throws IOException
     */
    private void writeLinePrefix() throws IOException {
        this.lineNumber++;
        this.linePrefixe = this.lineNumber.toString() + TAB;
        super.write(linePrefixe, 0, linePrefixe.length());
    }
}
