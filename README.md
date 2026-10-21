# Slideshow Optimisation — Greedy Java Solver

A Java solution to the slideshow optimisation problem from Google's Hash Code challenge. It constructs slides from horizontal and vertical photographs, then orders them to improve the interest score between consecutive slides.

## Approach

Horizontal photos become individual slides; vertical photos are paired with their tags combined. The ordering stage starts with one slide and greedily chooses the best-scoring next slide among up to 200 remaining candidates.

The transition score is the minimum of three counts: shared tags, tags unique to the first slide and tags unique to the second. `HashSet` is used for tag membership checks.

This is a bounded greedy heuristic, not an exact optimiser; the candidate limit trades potential solution quality for runtime.

## Run

Requires a JDK.

```bash
javac Main.java
java Main a_example.txt output.txt
```

The program accepts an input file and output path, writes the slideshow ordering and prints a score. Sample challenge inputs and generated outputs are included. Compiled `.class` files are not needed to build the program.

## Implementation

The solution is in `Main.java`, including input parsing, vertical pairing, candidate scoring, greedy ordering and output generation. It was developed as a university programming project.
