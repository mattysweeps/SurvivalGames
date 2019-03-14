package io.github.m0pt0pmatt.survivalgames;

import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Optional;

@ConfigSerializable
public class SurvivalGamesPluginConfig {

    public static ObjectMapper<SurvivalGamesPluginConfig> OBJECT_MAPPER;

    static {
        try {
            OBJECT_MAPPER = ObjectMapper.forClass(SurvivalGamesPluginConfig.class);
        } catch (ObjectMappingException e) {
            throw new RuntimeException(e);
        }
    }

    @Setting(value = "autoStartDemo")
    private Boolean autoStartDemo;

    public Optional<Boolean> getAutoStartDemo() {
        return Optional.ofNullable(autoStartDemo);
    }

    public void setAutoStartDemo(Boolean autoStartDemo) {
        this.autoStartDemo = autoStartDemo;
    }
}
