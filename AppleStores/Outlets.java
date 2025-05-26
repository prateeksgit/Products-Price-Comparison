package org.conversion.AppleStores;

import java.util.HashMap;

public class Outlets{
    public static class UKOutlet extends MainStore {
        public UKOutlet() {
            super("UK", 8.85, "GBP", new HashMap<>() {{
                put(IPHONE_16_PRO_256, 1209.0);
                put(IPHONE_16_PRO_MAX_256, 1300.0);
                put(AIRPODS_PRO_3RD_GEN, 400.45);
            }});

        }

    }
    public static class USAOutlet extends MainStore {
        public USAOutlet() {
            super("USA", 8.85, "USD", new HashMap<>() {{
                put(IPHONE_16_PRO_256, 1209.0);
                put(IPHONE_16_PRO_MAX_256, 1300.0);
                put(AIRPODS_PRO_3RD_GEN, 400.45);
            }});

        }

    }
    public static class JapanOutlet extends MainStore {
        public JapanOutlet() {
            super("JPN", 8.85, "YEN", new HashMap<>() {{
                put(IPHONE_16_PRO_256, 1209.0);
                put(IPHONE_16_PRO_MAX_256, 1300.0);
                put(AIRPODS_PRO_3RD_GEN, 400.45);
            }});

        }

    }
    public static class DubaiOutlet extends MainStore {
        public DubaiOutlet() {
            super("DUBAI", 8.85, "AED", new HashMap<>() {{
                put(IPHONE_16_PRO_256, 1209.0);
                put(IPHONE_16_PRO_MAX_256, 1300.0);
                put(AIRPODS_PRO_3RD_GEN, 400.45);
            }});

        }

    }
    public static class AustraliaOutlet extends MainStore {
        public AustraliaOutlet() {
            super("AUS", 8.85, "AUD", new HashMap<>() {{
                put(IPHONE_16_PRO_256, 1209.0);
                put(IPHONE_16_PRO_MAX_256, 1300.0);
                put(AIRPODS_PRO_3RD_GEN, 400.45);
            }});

        }

    }
}
