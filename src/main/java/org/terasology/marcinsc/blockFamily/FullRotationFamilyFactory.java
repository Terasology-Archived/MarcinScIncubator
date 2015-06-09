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
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockBuilderHelper;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.block.family.BlockFamilyFactory;
import org.terasology.world.block.family.RegisterBlockFamilyFactory;
import org.terasology.world.block.loader.BlockFamilyDefinition;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Block families created with this factory will contain a full set of rotations for the block.
 */
@RegisterBlockFamilyFactory("fullRotation")
public class FullRotationFamilyFactory implements BlockFamilyFactory {
    private static final Set<String> BLOCK_NAMES = new HashSet<>();

    static {
        for (Rotation rotation : Rotation.values()) {
            BLOCK_NAMES.add(rotation.getYaw().ordinal() + "." + rotation.getPitch().ordinal() + "." + rotation.getRoll().ordinal());
        }
    }

    @Override
    public Set<String> getSectionNames() {
        return Collections.unmodifiableSet(BLOCK_NAMES);
    }

    @Override
    public BlockFamily createBlockFamily(BlockFamilyDefinition definition, BlockBuilderHelper blockBuilder) {
        Map<Rotation, Block> blocksByRotation = Maps.newHashMap();
        for (Rotation rot : Rotation.values()) {
            blocksByRotation.put(rot, blockBuilder.constructTransformedBlock(definition, rot));
        }

        return new FullRotationFamily(new BlockUri(definition.getUrn()), definition.getCategories(), Rotation.none(), blocksByRotation);
    }
}
