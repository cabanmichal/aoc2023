package utils

class Utils {
    static File getFile(Integer day) {
        String path = sprintf("input/aoc2023_%02d_input.txt", day)
        URL resource = Utils.class.getClassLoader().getResource(path)
        return new File(resource.toURI())
    }
}
