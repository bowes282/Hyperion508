package org.hyperion.rs2.net.codec;

/**
 * This is a shit way of doing it, may create a better way later on.
 *
 * @author Linux
 */
public class RS2WorldList {

    /**
     * The activity (What type of World it is)
     */
    private final String activity;
    /**
     * The country
     */
    private final int country;
    /**
     * The flag (What type of server it is)
     */
    private final int flag;
    /**
     * The IP Address of the world
     */
    private final String ip;
    /**
     * The location
     */
    private final int location;
    /**
     * The region of the server
     */
    private final String region;
    /**
     * The World id
     */
    private final int worldId;

    /**
     * The WorldList constructor.
     *
     * @param worldId The WorldId
     * @param location The location
     * @param flag The flag
     * @param activity The activity
     * @param ip The IP
     * @param region The region
     * @param country The country
     */
    public RS2WorldList(int worldId, int location, int flag, String activity,
            String ip, String region, int country) {
        this.worldId = worldId;
        this.location = location;
        this.flag = flag;
        this.activity = activity;
        this.ip = ip;
        this.region = region;
        this.country = country;
    }

    /**
     * Gets the activity
     *
     * @return The activity
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Gets the country
     *
     * @return The country
     */
    public int getCountry() {
        return country;
    }

    /**
     * The flag
     *
     * @return Gets the flag
     */
    public int getFlag() {
        return flag;
    }

    /**
     * Gets the IP Address
     *
     * @return The Ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * Gets the Location
     *
     * @return The location
     */
    public int getLocation() {
        return location;
    }

    /**
     * Gets the region
     *
     * @return The region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Gets the world Id
     *
     * @return The WorldId
     */
    public int getWorldId() {
        return worldId;
    }
}