package org.example.springboot;

/**
 * @author dizhang
 * @date 2021-05-12
 */
@FunctionalInterface
public interface HelloInterface {

    HelloInterface DEFAULT = info -> {
        try {
            switch (info) {
                case "AA":
                    return "AA";
                case "BB":
                    return "BB";
                default:
                    return "default";
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    };

    String msg(String info);
}
