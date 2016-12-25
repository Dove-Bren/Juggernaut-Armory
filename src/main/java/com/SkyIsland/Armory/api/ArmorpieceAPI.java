/* 
 * While there is no official API for creating new pieces of armor,
 * it's still possible to add your own. You can, for example, create
 * a different type of torso armor that only has a breastplate and a gorget.
 * 
 * The following is a direct snippet from the ArmorTorso class, which
 * does exactly what is explained above: it creates a set of components
 * and registers them. Making additional pieces of armor (and the
 * components that make it up) are done in the same manner.
 * 
 * More specifically, the below creates a piece of armor made to be
 * worn in the torso slot. It creates one component of the armor --
 * the breastplate -- and registers it. 
 * 
 */

//	public ArmorTorso(String unlocalizedName) {
//		super(ArmorSlot.TORSO, unlocalizedName);
//		pieces = new EnumMap<Slot, ArmorPiece>(Slot.class);
//		
//		
//		//initialize slot pieces
//		Map<DamageType, Float> pieceContribution;
//		boolean[][] metalMap;
//		ArmorPiece piece;
//		IForgeTemplate recipe;
//		
//		pieceContribution = DamageType.freshMap();
//			pieceContribution.put(DamageType.SLASH, 0.40f);
//			pieceContribution.put(DamageType.PIERCE, 0.60f);
//			pieceContribution.put(DamageType.CRUSH, 0.50f);
//			pieceContribution.put(DamageType.MAGIC, 0.10f);
//			pieceContribution.put(DamageType.OTHER, 0.20f);
//		piece = new ArmorPiece("breastplate", ArmorSlot.TORSO, pieceContribution, 1.2f);
//		piece.setyOffset(.4f);
//		pieces.put(Slot.BREASTPLATE, piece);
//		metalMap = ForgeRecipe.drawMap(new String[]{
//			" ", "  ..  ..", "  ......", "   ....", "   ....", "   ....",
//			"    ..", "   ....", "  ......", " "
//		});
//		
//		recipe = new ArmorPieceRecipe(piece);
//		ForgeManager.instance().registerForgeRecipe(new ForgeRecipe(
//				"Breastplate", -5, metalMap, recipe
//				));
//		
//	}