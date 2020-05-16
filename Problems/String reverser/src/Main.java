StringReverser reverser = new StringReverser() {
    @Override
public String reverse(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--) {
            result.append(str.charAt(i));
        }
        return result.toString();
        }
        };