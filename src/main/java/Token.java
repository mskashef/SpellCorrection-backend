class Token {
    private final int count;
    private final String token;

    public Token(String token, int count) {
        this.token = token;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return token + "=" + count;
    }

    public double getSimilarity(String token) {
        return NLP.getSimilariy(this.getToken(), token);
    }

    public int getDistance(String token) {
        return NLP.minDistance(this.getToken(), token);
    }

    public double getDoubleCheckedSimilarity(String token) {
        return NLP.getSimilariy(this.getToken(), token) / NLP.minDistance(this.getToken(), token);
    }
}
