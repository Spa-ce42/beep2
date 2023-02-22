package demo;

import org.millburn.e24feik.beep2.util.Point;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class SpaceContextLoader implements Closeable {
    private final BufferedReader br;

    public SpaceContextLoader(String file) {
        try {
            this.br = new BufferedReader(new FileReader(file));
        } catch(FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Point readCoordinatesLine() throws IOException {
        String line = this.br.readLine();
        String[] numbers = line.split(",");

        return new Point(
                Integer.parseInt(numbers[0]),
                Integer.parseInt(numbers[1]),
                Integer.parseInt(numbers[2])
        );
    }

    private int readIntLine() throws IOException {
        return Integer.parseInt(this.br.readLine());
    }

    public SpaceContext load() {
        SpaceContext sc;

        try {
            String line = this.br.readLine();
            String[] numbers = line.split(",");

            sc = new SpaceContext(
                    Integer.parseInt(numbers[0]),
                    Integer.parseInt(numbers[1]),
                    Integer.parseInt(numbers[2])
            );

            List<Point> exits = new ArrayList<>();

            for(int i = 0; i < 15; ++i) {
                exits.add(readCoordinatesLine());
            }

            List<Point> bees = new ArrayList<>();

            for(int i = 0; i < 15; ++i) {
                bees.add(readCoordinatesLine());
            }

            List<Point> obstacles = new ArrayList<>();

            for(int i = 0, n = this.readIntLine(); i < n; ++i) {
                obstacles.add(readCoordinatesLine());
            }

            sc.setBees(bees);
            sc.setObstacles(obstacles);
            sc.setExits(exits);
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }

        return sc;
    }

    @Override
    public void close() {
        try {
            this.br.close();
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
