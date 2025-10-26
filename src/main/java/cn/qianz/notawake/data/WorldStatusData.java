package cn.qianz.notawake.data;

import net.minecraft.nbt.CompoundTag;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import static cn.qianz.notawake.Notawake.MODID;


public class WorldStatusData extends SavedData {
    public static final String FIRST_SLEEP_DONE_KEY = "firstSleepDone";
    private boolean firstSleepDone = false;


    public WorldStatusData() {}

    public static WorldStatusData load(CompoundTag tag) {
        WorldStatusData data = new WorldStatusData();
        data.firstSleepDone = tag.getBoolean(FIRST_SLEEP_DONE_KEY);
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean(FIRST_SLEEP_DONE_KEY, firstSleepDone);
        return tag;
    }

    public void setFirstSleepDone(boolean value) {
        this.firstSleepDone = value;
        this.setDirty();
    }

    public boolean getFirstSleepDone() {
        return firstSleepDone;
    }

    public static WorldStatusData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                WorldStatusData::load,
                WorldStatusData::new,
                MODID+":world_status"
        );
    }
}