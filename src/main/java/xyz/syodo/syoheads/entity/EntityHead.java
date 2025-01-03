package xyz.syodo.syoheads.entity;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntitySkull;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.data.EntityDataType;
import cn.nukkit.entity.data.EntityDataTypes;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;

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
            this.server.updatePlayerListData(this.getUniqueId(), this.getId(), this.getName(), this.checkSkin(this.skin), new Player[]{player});

            AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
            addPlayerPacket.uuid = this.getUniqueId();
            addPlayerPacket.username = this.getName();
            addPlayerPacket.entityUniqueId = this.getId();
            addPlayerPacket.entityRuntimeId = this.getId();
            if(!move) {
                addPlayerPacket.x = (float) this.x;
                addPlayerPacket.y = (float) this.y;
                addPlayerPacket.z = (float) this.z;
                addPlayerPacket.speedX = (float) this.motionX;
                addPlayerPacket.speedY = (float) this.motionY;
                addPlayerPacket.speedZ = (float) this.motionZ;
                addPlayerPacket.yaw = (float) this.yaw;
                addPlayerPacket.pitch = (float) this.pitch;
            }
            addPlayerPacket.item = this.inventory.getItemInHand();
            addPlayerPacket.entityData = this.entityDataMap.copy(this.entityDataMap.keySet().toArray(new EntityDataType[this.entityDataMap.keySet().size()]));
            player.dataPacket(addPlayerPacket);

            this.server.removePlayerListData(this.getUniqueId(), new Player[]{player});

            if(move) {
                MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
                movePlayerPacket.eid = this.getId();
                movePlayerPacket.x = (float) this.x;
                movePlayerPacket.y = (float) this.y;
                movePlayerPacket.z = (float) this.z;
                movePlayerPacket.headYaw = (float) this.headYaw;
                movePlayerPacket.yaw = (float) this.yaw;
                player.dataPacket(movePlayerPacket);
            }
        }
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        setScale(1.1f);
        this.setDataProperty(EntityDataTypes.HEIGHT, 0f);
        this.setDataProperty(EntityDataTypes.WIDTH, 0f);
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
    public boolean attack(float damage) {
        return false;
    }

    private Skin checkSkin(Skin skin) {
        skin.setTrusted(true);
        if (!skin.isPersona()) {
            skin.setFullSkinId(UUID.nameUUIDFromBytes(skin.getSkinData().data).toString());
        }
        return skin;
    }

    public static EntityHead create(BlockEntitySkull blockEntitySkull) {
        CompoundTag tag;
        if(blockEntitySkull.namedTag.containsCompound("HeadEntityData")) {
            tag = blockEntitySkull.namedTag.getCompound("HeadEntityData");
        } else throw new RuntimeException("BlockEntitySkull has no SkullEntity Data");
        EntityHead head = new EntityHead(blockEntitySkull.getChunk(), tag);
        head.spawnToAll();
        return head;
    }

}

