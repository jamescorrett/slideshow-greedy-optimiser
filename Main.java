import java.io.*;
import java.util.*;

public class Main {

    static final int SEARCH_LIMIT = 200;

    static class Photo {
        int id;
        char orientation;
        HashSet<String> tags;

        Photo(int id, char orientation, HashSet<String> tags) {
            this.id = id;
            this.orientation = orientation;
            this.tags = tags;
        }
    }

    static class Slide {
        int photo1;
        int photo2;
        HashSet<String> tags;

        Slide(int photo1, int photo2, HashSet<String> tags) {
            this.photo1 = photo1;
            this.photo2 = photo2;
            this.tags = tags;
        }

        boolean hasTwoPhotos() {
            return photo2 != -1;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Use java Main.java input.txt output.txt");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];

        List<Photo> photos = readPhotos(inputFile);
        List<Slide> slides = buildSlideshow(photos);

        writeOutput(slides, outputFile);

        System.out.println("Photos read: " + photos.size());
        System.out.println("Slides created: " + slides.size());
        System.out.println("Score: " + totalScore(slides));
    }

    static List<Photo> readPhotos(String filename) throws IOException {
        List<Photo> photos = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));

        int n = Integer.parseInt(br.readLine().trim());

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            char orientation = st.nextToken().charAt(0);
            int tagCount = Integer.parseInt(st.nextToken());
            HashSet<String> tags = new HashSet<>();

            for (int j = 0; j < tagCount; j++) {
                tags.add(st.nextToken());
            }

            photos.add(new Photo(i, orientation, tags));
        }

        br.close();
        return photos;
    }

    static List<Slide> buildSlideshow(List<Photo> photos) {
        List<Photo> horizontals = new ArrayList<>();
        List<Photo> verticals = new ArrayList<>();

        for (Photo photo : photos) {
            if (photo.orientation == 'H') {
                horizontals.add(photo);
            } else {
                verticals.add(photo);
            }
        }

        List<Slide> slides = new ArrayList<>();

        for (Photo photo : horizontals) {
            slides.add(new Slide(photo.id, -1, new HashSet<>(photo.tags)));
        }

        for (int i = 0; i + 1 < verticals.size(); i += 2) {
            Photo p1 = verticals.get(i);
            Photo p2 = verticals.get(i + 1);

            HashSet<String> combinedTags = new HashSet<>(p1.tags);
            combinedTags.addAll(p2.tags);

            slides.add(new Slide(p1.id, p2.id, combinedTags));
        }

        return reorderSlidesGreedy(slides);
    }

    static List<Slide> reorderSlidesGreedy(List<Slide> slides) {
        if (slides.isEmpty()) {
            return slides;
        }

        List<Slide> remaining = new ArrayList<>(slides);
        List<Slide> ordered = new ArrayList<>();

        Slide current = remaining.remove(0);
        ordered.add(current);

        while (!remaining.isEmpty()) {
            int bestIndex = 0;
            int bestScore = -1;

            int limit = Math.min(SEARCH_LIMIT, remaining.size());

            for (int i = 0; i < limit; i++) {
                int score = interestFactor(current, remaining.get(i));

                if (score > bestScore) {
                    bestScore = score;
                    bestIndex = i;
                }
            }

            current = remaining.remove(bestIndex);
            ordered.add(current);
        }

        return ordered;
    }

    static int interestFactor(Slide a, Slide b) {
        int common = 0;

        for (String tag : a.tags) {
            if (b.tags.contains(tag)) {
                common++;
            }
        }

        int onlyA = a.tags.size() - common;
        int onlyB = b.tags.size() - common;

        return Math.min(common, Math.min(onlyA, onlyB));
    }

    static long totalScore(List<Slide> slides) {
        long total = 0;

        for (int i = 0; i < slides.size() - 1; i++) {
            total += interestFactor(slides.get(i), slides.get(i + 1));
        }

        return total;
    }

    static void writeOutput(List<Slide> slides, String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

        bw.write(String.valueOf(slides.size()));
        bw.newLine();

        for (Slide slide : slides) {
            if (slide.hasTwoPhotos()) {
                bw.write(slide.photo1 + " " + slide.photo2);
            } else {
                bw.write(String.valueOf(slide.photo1));
            }
            bw.newLine();
        }

        bw.close();
    }
}