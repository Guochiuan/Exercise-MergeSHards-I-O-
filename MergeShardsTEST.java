import java.io.BufferedReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public final class MergeShardsTEST {
  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.out.println("Usage: MergeShards [input folder] [output file]");
      return;
    }

    List<Path> inputs = Files.walk(Path.of(args[0]), 1).skip(1).collect(Collectors.toList());
    List<BufferedReader> readers = new ArrayList<>(inputs.size());
    Path outputPath = Path.of(args[1]);

    try {
      for (Path input : inputs) {
        readers.add(Files.newBufferedReader(input));
      }
      PriorityQueue<WordEntry> words = new PriorityQueue<>();
      int c = 0;
      for (BufferedReader reader : readers) {



        String word = reader.readLine();
        System.out.println(word);
        if (word != null) {
          words.add(new WordEntry(word, reader));
          c++;
          System.out.println(c);
        }
      }

      try (Writer writer = Files.newBufferedWriter(outputPath)) {
        int d=0;

        int e=0;
        while (!words.isEmpty()) {
          WordEntry entry = words.poll();
          writer.write(entry.word);

          int ccc = words.size();
          System.out.println("size====     "+         ccc);

          System.out.println("write            " + entry.word);
          writer.write(System.lineSeparator());

          d++;
          System.out.println("count     " + d);



          String word = entry.reader.readLine();
          System.out.println("words to read:                " + word);
          if (word != null) {
            e++;
            words.add(new WordEntry(word, entry.reader));
            System.out.println("e:     " + e   );
            System.out.println(word);
          }
        }
      }
    } finally {
      for (BufferedReader reader : readers) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static final class WordEntry implements Comparable<WordEntry> {
    private final String word;
    private final BufferedReader reader;

    private WordEntry(String word, BufferedReader reader) {
      this.word = Objects.requireNonNull(word);
      this.reader = Objects.requireNonNull(reader);
    }

    @Override
    public int compareTo(WordEntry other) {
      return word.compareTo(other.word);
    }
  }
}