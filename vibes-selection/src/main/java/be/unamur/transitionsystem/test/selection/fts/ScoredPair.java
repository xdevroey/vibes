package be.unamur.transitionsystem.test.selection.fts;

class ScoredPair<T, U> implements Comparable<ScoredPair<T, U>> {

    private int score;
    private T first;
    private U second;

    public ScoredPair(int score, T first, U second) {
        this.score = score;
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(ScoredPair<T, U> o) {
        return score - o.score;
    }

}
