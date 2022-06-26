package Functions.Configuration;

public class AlgorandConfiguration {
    private final long assetId;
    private final String assetName;
    private final String assetUnitName;

    private final short assetDecimalPlaces;
    private final double amountToEarnPerHour;
    private final int[] serverGroupIdsThatCanEarn;
    private final String mnemonicSeedOfReserveAccount;
    private final String ALGOD_API_ADDR;
    private final String ALGOD_API_TOKEN;
    private final String ALGOD_API_TOKEN_KEY;
    private final int ALGOD_API_PORT;
    private final AlgorandAsaShopItem[] items;

    public AlgorandConfiguration(long assetId, String assetName, String assetUnitName, short assetDecimalPlaces, double amountToEarnPerHour, int[] serverGroupIdsThatCanEarn, String mnemonicSeedOfReserveAccount, String ALGOD_API_ADDR, String ALGOD_API_TOKEN, String ALGOD_API_TOKEN_KEY, int ALGOD_API_PORT, AlgorandAsaShopItem[] items) {
        this.assetId = assetId;
        this.assetName = assetName;
        this.assetUnitName = assetUnitName;
        this.assetDecimalPlaces = assetDecimalPlaces;
        this.amountToEarnPerHour = amountToEarnPerHour;
        this.serverGroupIdsThatCanEarn = serverGroupIdsThatCanEarn;
        this.mnemonicSeedOfReserveAccount = mnemonicSeedOfReserveAccount;
        this.ALGOD_API_ADDR = ALGOD_API_ADDR;
        this.ALGOD_API_TOKEN = ALGOD_API_TOKEN;
        this.ALGOD_API_TOKEN_KEY = ALGOD_API_TOKEN_KEY;
        this.ALGOD_API_PORT = ALGOD_API_PORT;
        this.items = items;
    }

    public long getAssetId() {
        return assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public String getAssetUnitName() {
        return assetUnitName;
    }

    public short getAssetDecimalPlaces() {
        return assetDecimalPlaces;
    }

    public double getAmountToEarnPerHour() {
        return amountToEarnPerHour;
    }

    public int[] getServerGroupIdsThatCanEarn() {
        return serverGroupIdsThatCanEarn;
    }

    public String getMnemonicSeedOfReserveAccount() {
        return mnemonicSeedOfReserveAccount;
    }

    public String getALGOD_API_ADDR() {
        return ALGOD_API_ADDR;
    }

    public String getALGOD_API_TOKEN() {
        return ALGOD_API_TOKEN;
    }

    public String getALGOD_API_TOKEN_KEY() {
        return ALGOD_API_TOKEN_KEY;
    }

    public int getALGOD_API_PORT() {
        return ALGOD_API_PORT;
    }

    public AlgorandAsaShopItem[] getItems() {
        return items;
    }
}
