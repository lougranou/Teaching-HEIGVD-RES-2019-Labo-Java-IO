package ch.heigvd.res.labio.impl.filters;

import java.io.BufferedInputStream;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
      String[] lignes = str.split("\n");
      StringBuilder stringBuilder = new StringBuilder();
      int indexLigne = 1;

      for(int i = 0; i < lignes.length ; ++i){
          stringBuilder.append(indexLigne).append("\t").append(lignes[i]).append("\n");
          ++indexLigne;
      }

  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    write(cbuf.toString(), off, len);
  }

  @Override
  public void write(int c) throws IOException {
      char[] tmp = new char[1];
      tmp[0] = (char) c;

    write(tmp,0,1);
  }

}
