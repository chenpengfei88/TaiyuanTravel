package com.chenpengfei.taiyuantravel.pojo;

import java.util.ArrayList;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description  线路方案实体类
 */
public class StationProgramme {

    private String programmeName; //线路名称
    private int stationTime; //线路时间
    private String walkLength; //步行长度
    private String stationCount; //站的数量

    private ArrayList<StationProgramme> childStationProgrammeList;

    public StationProgramme(String programmeName) {
        this.programmeName = programmeName;
    }

    public StationProgramme() {
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public ArrayList<StationProgramme> getChildStationProgrammeList() {
        return childStationProgrammeList;
    }

    public void setChildStationProgrammeList(ArrayList<StationProgramme> childStationProgrammeList) {
        this.childStationProgrammeList = childStationProgrammeList;
    }

    public String getWalkLength() {
        return walkLength;
    }

    public void setWalkLength(String walkLength) {
        this.walkLength = walkLength;
    }

    public String getStationCount() {
        return stationCount;
    }

    public void setStationCount(String stationCount) {
        this.stationCount = stationCount;
    }

    public int getStationTime() {
        return stationTime;
    }

    public void setStationTime(int stationTime) {
        this.stationTime = stationTime;
    }
}
