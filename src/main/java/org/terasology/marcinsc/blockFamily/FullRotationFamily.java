/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.marcinsc.blockFamily;

import com.google.common.collect.Maps;
import org.terasology.math.Rotation;
import org.terasology.math.Side;
import org.terasology.math.geom.Vector3i;
import org.terasology.naming.Name;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.AbstractBlockFamily;

import java.util.Map;

public class FullRotationFamily extends AbstractBlockFamily implements RotationBlockFamily {
    private Map<Rotation, Block> blocks;

    private Block archetypeBlock;

    private Map<BlockUri, Block> blockUriMap = Maps.newHashMap();

    public FullRotationFamily(BlockUri uri, Iterable<String> categories, Rotation archetypeRotation,
                              Map<Rotation, Block> blocks) {
        super(uri, categories);
        this.blocks = blocks;

        for (Map.Entry<Rotation, Block> rotationBlockEntry : blocks.entrySet()) {
            Rotation rotation = rotationBlockEntry.getKey();
            Block block = rotationBlockEntry.getValue();
            block.setBlockFamily(this);
            BlockUri blockUri = new BlockUri(uri, new Name(rotation.getYaw().ordinal() + "." + rotation.getPitch().ordinal() + "." + rotation.getRoll().ordinal()));
            block.setUri(blockUri);
            blockUriMap.put(blockUri, block);
        }

        archetypeBlock = blocks.get(archetypeRotation);
    }

    @Override
    public Block getBlockForPlacement(WorldProvider worldProvider, BlockEntityRegistry blockEntityRegistry, Vector3i location, Side attachmentSide, Side direction) {
        // Find first one so that FRONT Side of the original block is same as attachmentSide
        for (Map.Entry<Rotation, Block> rotationBlockEntry : blocks.entrySet()) {
            if (rotationBlockEntry.getKey().rotate(Side.FRONT) == attachmentSide) {
                return rotationBlockEntry.getValue();
            }
        }

        return null;
    }

    @Override
    public Block getArchetypeBlock() {
        return archetypeBlock;
    }

    @Override
    public Block getBlockFor(BlockUri blockUri) {
        return blockUriMap.get(blockUri);
    }

    @Override
    public Iterable<Block> getBlocks() {
        return blocks.values();
    }

    @Override
    public Block getBlockForRotation(Rotation rotation) {
        return blocks.get(rotation);
    }

    @Override
    public Rotation getRotation(Block block) {
        return findRotationForBlock(block);
    }

    private Rotation findRotationForBlock(Block block) {
        for (Map.Entry<Rotation, Block> rotationBlockEntry : blocks.entrySet()) {
            if (rotationBlockEntry.getValue() == block) {
                return rotationBlockEntry.getKey();
            }
        }
        return null;
    }
}
