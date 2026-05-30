package xyz.syodo.syoheads.entity;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntitySkull;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.data.human.Skin;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.*;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.BuildPlatform;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.actor.ActorDataTypes;
import org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket;

import java.awt.Color;
import java.util.UUID;

public class EntityHead extends EntityHuman implements CustomEntity {

    private static final AxisAlignedBB EMPTY_BOUNDING_BOX = new SimpleAxisAlignedBB(new Vector3(), new Vector3());

    public EntityHead(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public void spawnToAll() {
        if (this.chunk != null && !this.closed) {
            for(Player player : this.level.getChunkPlayers(this.chunk.getX(), this.chunk.getZ()).values()) {
                if (player.isOnline()) {
                    this.spawnTo(player, true);
                }
            }
        }
    }

    @Override
    public void spawnTo(Player player) {
        this.spawnTo(player, false);
    }

    public void spawnTo(Player player, boolean move) {
        if (!this.hasSpawned.containsKey(player.getLoaderId())) {
            this.hasSpawned.put(player.getLoaderId(), player);
            this.server.updatePlayerListData(this.getUniqueId(), this.getId(), this.getName(), this.checkSkin(this.skin), Color.WHITE, new Player[]{player});

            this.actorDataMap.put(ActorDataTypes.RESERVED_139, 0L);
            this.actorDataMap.put(ActorDataTypes.NAMEPLATE_RENDER_DISTANCE_MAX, 64f);

            AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
            addPlayerPacket.setActorData(this.actorDataMap);
            addPlayerPacket.setUuid(this.getUniqueId());
            addPlayerPacket.setPlayerName(this.getName());
            addPlayerPacket.setTargetActorID(this.getId());
            addPlayerPacket.setTargetRuntimeID(this.getId());
            addPlayerPacket.setPosition(Vector3f.from(this.x, this.y, this.z));
            addPlayerPacket.setVelocity(Vector3f.from(this.motionX, this.motionY, this.motionZ));
            addPlayerPacket.setRotation(Vector3f.from(this.pitch, this.yaw, this.headYaw));
            addPlayerPacket.setCarriedItem(this.getInventory().getItemInMainHand().toNetwork());
            addPlayerPacket.setDeviceId("");
            addPlayerPacket.setPlatformChatId("");
            addPlayerPacket.setBuildPlatform(BuildPlatform.UNKNOWN);
            addPlayerPacket.setPlayerGameType(GameType.SURVIVAL);
            addPlayerPacket.setAbilitiesData(this.buildSerializedAbilitiesData());
            player.sendPacket(addPlayerPacket);

            this.server.removePlayerListData(this.getUniqueId(), player);
        }


    }

    @Override
    protected void initEntity() {
        super.initEntity();
        setScale(1.1f);
        this.setDataProperty(ActorDataTypes.HEIGHT, 0f);
        this.setDataProperty(ActorDataTypes.WIDTH, 0f);
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public float getWidth() {
        return 0f;
    }

    @Override
    public float getHeight() {
        return 0f;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return EMPTY_BOUNDING_BOX;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        return false;
    }

    private Skin checkSkin(Skin skin) {
        skin.setTrusted(true);
        if (!skin.getSkin().isPersona()) {
            skin = new Skin(skin.getSkin().toBuilder()
                    .fullSkinId(UUID.nameUUIDFromBytes(skin.getSkin().getSkinData().getImage()).toString())
                    .build(), true);
            setSkin(skin);
        }
        return skin;
    }

    public static EntityHead create(BlockEntitySkull blockEntitySkull) {
        CompoundTag tag;
        if(blockEntitySkull.getNbt().containsCompound("HeadEntityData")) {
            tag = blockEntitySkull.getNbt().getCompound("HeadEntityData");
        } else throw new RuntimeException("BlockEntitySkull has no SkullEntity Data");
        EntityHead head = new EntityHead(blockEntitySkull.getChunk(), tag);
        head.spawnToAll();
        return head;
    }

}
