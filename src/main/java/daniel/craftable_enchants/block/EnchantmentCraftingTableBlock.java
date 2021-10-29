package daniel.craftable_enchants.block;

import daniel.craftable_enchants.screen.EnchantmentCraftingScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnchantmentCraftingTableBlock extends Block {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final Text TITLE = new TranslatableText("container.enchantment_crafting");

    public static final VoxelShape BASE_PLATE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);

    public static final VoxelShape BASE_COLUMN_NORTH = Block.createCuboidShape(4.0, 6.0, 3.0, 12.0, 14.8, 12.0);
    public static final VoxelShape BASE_COLUMN_SOUTH = Block.createCuboidShape(4.0, 6.0, 4.0, 12.0, 14.8, 13.0);
    public static final VoxelShape BASE_COLUMN_EAST= Block.createCuboidShape(4.0, 6.0, 4.0, 13.0, 14.8, 12.0);
    public static final VoxelShape BASE_COLUMN_WEST = Block.createCuboidShape(3.0, 6.0, 4.0, 12.0, 14.8, 12.0);

    //Values generated with Blockbench plugin Mod Utils originally made by me and currently maintained by JTK222. This is quite cringe tbh.
    public static final VoxelShape TABLE_NORTH = VoxelShapes.union(
            Block.createCuboidShape(0, 10.903565501678102, 0.9105120539312768, 16, 14.903565501678102, 5.210512085720419),
            Block.createCuboidShape(0, 12.903565501678102, 5.210512053931277, 16, 16.903565501678102, 9.54384538726461),
            Block.createCuboidShape(0, 15.153565501678102, 9.460512053931277, 16, 19.153565501678102, 13.79384538726461));

    public static final VoxelShape TABLE_EAST = VoxelShapes.union(
            Block.createCuboidShape(10.749999968210858, 10.903565501678102, -0.039487946068724966, 15.049999999999997, 14.903565501678102, 15.960512053931275),
            Block.createCuboidShape(6.416666666666664, 12.903565501678102, -0.039487946068724966, 10.75, 16.903565501678102, 15.960512053931275              ),
            Block.createCuboidShape(2.1666666666666643, 15.153565501678102, -0.039487946068724966, 6.5, 19.153565501678102, 15.960512053931275               ));

    public static final VoxelShape TABLE_WEST = VoxelShapes.union(
            Block.createCuboidShape(0.9555555502573672, 10.903565501678102, 0.06606760418864077, 5.255555582046506, 14.903565501678102, 16.06606760418864),
            Block.createCuboidShape(5.255555550257364, 12.903565501678102, 0.06606760418864077, 9.5888888835907, 16.903565501678102, 16.06606760418864   ),
            Block.createCuboidShape(9.505555550257364, 15.153565501678102, 0.06606760418864077, 13.8388888835907, 19.153565501678102, 16.06606760418864  ));

    public static final VoxelShape TABLE_SOUTH = VoxelShapes.union(
            Block.createCuboidShape(0, 10.903565501678102, 10.710512022142133, 16, 14.903565501678102, 15.010512053931272),
            Block.createCuboidShape(0, 12.903565501678102, 6.377178720597939, 16, 16.903565501678102, 10.710512053931275 ),
            Block.createCuboidShape(0, 15.153565501678102, 2.1271787205979393, 16, 19.153565501678102, 6.460512053931275 ));

    public static final VoxelShape NORTH = VoxelShapes.union(VoxelShapes.union(BASE_PLATE, BASE_COLUMN_NORTH), TABLE_NORTH);
    public static final VoxelShape SOUTH = VoxelShapes.union(VoxelShapes.union(BASE_PLATE, BASE_COLUMN_SOUTH), TABLE_SOUTH);
    public static final VoxelShape EAST = VoxelShapes.union(VoxelShapes.union(BASE_PLATE, BASE_COLUMN_EAST), TABLE_EAST);
    public static final VoxelShape WEST = VoxelShapes.union(VoxelShapes.union(BASE_PLATE, BASE_COLUMN_WEST), TABLE_WEST);


    public EnchantmentCraftingTableBlock(Settings settings) {
        super(settings);

        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockSoundGroup getSoundGroup(BlockState state) {
        return BlockSoundGroup.COPPER;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(FACING)) {
            case NORTH:
                return NORTH;
            case EAST:
                return EAST;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            default:
                return BASE_PLATE;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            //I probably should have added a statistic to count interactions with this, but it's a PITA and useless so instead there is this comment
            return ActionResult.CONSUME;
        }
    }



    @Nullable
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new EnchantmentCraftingScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), TITLE);
    }
}
