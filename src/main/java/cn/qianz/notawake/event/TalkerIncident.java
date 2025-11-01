package cn.qianz.notawake.event;


import cn.qianz.notawake.data.WorldStatusData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;

import java.util.*;

/**
 *
 */
public class TalkerIncident {
    private static final Queue<TalkWord> talkWords = new ArrayDeque<>();
    private static final int guidOverTick;


    static {
        talkWords.add(new TalkWord(1000, Component.translatable("message.notawake.talker.join").getString(), "yellow"));
        talkWords.add(new TalkWord(1100, Component.translatable("message.notawake.talker.guide1").getString()));
        talkWords.add(new TalkWord(1150, Component.translatable("message.notawake.talker.guide2").getString()));
        talkWords.add(new TalkWord(1300, Component.translatable("message.notawake.talker.guide3").getString()));
        talkWords.add(new TalkWord(1500, Component.translatable("message.notawake.talker.guide4").getString()));
        talkWords.add(new TalkWord(1660, Component.translatable("message.notawake.talker.guide5").getString()));
        talkWords.add(new TalkWord(1800, Component.translatable("message.notawake.talker.guide6").getString()));
        talkWords.add(new TalkWord(5100, Component.translatable("message.notawake.talker.guide7").getString()));
        talkWords.add(new TalkWord(5220, Component.translatable("message.notawake.talker.guide8").getString()));
        talkWords.add(new TalkWord(7000, Component.translatable("message.notawake.talker.guide9").getString()));
        guidOverTick = 7001;
    }

    public static void talkAllGuideWords(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide()) return;
        ServerLevel level = (ServerLevel) event.level;
        long gameTime = level.getGameTime();

        if (gameTime > guidOverTick) return;

        boolean needDelete = false;
        for (TalkWord talkWord : talkWords) {
            if (talkWord.getTalkTick() == gameTime) {
                talkToAllWorldPlayer(level, talkWord.getContent(), talkWord.getColor());
                needDelete = true;
            }
        }
        if (needDelete) {
            talkWords.remove();
        }
    }

    public static void firstWakeUp(PlayerWakeUpEvent event) {
        Player player = event.getEntity();
        Level level2 = player.level();
        if (level2.isClientSide()) return;
        ServerLevel level = (ServerLevel) level2;
        WorldStatusData wsd = WorldStatusData.get(level);
        if (!wsd.getFirstSleepDone()) {
            wsd.setFirstSleepDone(true);
            talkToAllWorldPlayer(level, Component.translatable("message.notawake.talker.first_wake_up").getString(), "write");
        }
    }

    private static void talkToAllWorldPlayer(Level level, String content, String color) {
        if (level.isClientSide) return;

        MutableComponent message = Component.literal(content);

        if (color != null && !color.isEmpty()) {
            try {
                TextColor textColor = TextColor.parseColor(color);
                message.withStyle(style -> style.withColor(textColor));
            } catch (Exception e) {
                System.err.println("Invalid color format: " + color);
            }
        }

        if (level instanceof ServerLevel serverLevel) {
            for (ServerPlayer player : serverLevel.players()) {
                player.sendSystemMessage(message);
            }
        }
    }

    public static class TalkWord {
        private final long talkTick;
        private final String color;
        private final String content;

        public TalkWord(long talkTick, String content, String color) {
            this.talkTick = talkTick;
            this.color = color;
            this.content = content;
        }

        public TalkWord(long talkTick, String content) {
            this.talkTick = talkTick;
            this.color = "write";
            this.content = content;
        }

        public long getTalkTick() {
            return talkTick;
        }

        public String getColor() {
            return color;
        }

        public String getContent() {
            return content;
        }
    }
}
