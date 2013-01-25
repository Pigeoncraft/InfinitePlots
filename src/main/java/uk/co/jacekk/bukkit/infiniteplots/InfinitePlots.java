package uk.co.jacekk.bukkit.infiniteplots;

import java.io.File;

import org.bukkit.generator.ChunkGenerator;

import uk.co.jacekk.bukkit.baseplugin.v8.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.v8.config.PluginConfig;
import uk.co.jacekk.bukkit.infiniteplots.command.ClaimCommandExecutor;
import uk.co.jacekk.bukkit.infiniteplots.flag.RestrictSpawningListener;
import uk.co.jacekk.bukkit.infiniteplots.generation.PlotsGenerator;
import uk.co.jacekk.bukkit.infiniteplots.plot.PlotManager;

public class InfinitePlots extends BasePlugin {
	
	private static InfinitePlots instance;
	
	private File plotsDir;
	private PlotManager plotManager;
	
	public void onEnable(){
		super.onEnable(true);
		
		instance = this;
		
		this.plotsDir =  new File(this.baseDirPath + File.separator + "plots");
		
		if (!this.plotsDir.exists()){
			this.plotsDir.mkdirs();
		}
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.class, this.log);
		
		this.plotManager = new PlotManager(this);
		
		this.pluginManager.registerEvents(new RestrictSpawningListener(this), this);
		this.pluginManager.registerEvents(new WorldInitListener(this), this);
		
		if (!this.config.getBoolean(Config.CLASSIC_MODE)){
			this.commandManager.registerCommandExecutor(new ClaimCommandExecutor(this));
		}
	}
	
	public void onDisable(){
		instance = null;
	}
	
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id){
		int size = this.config.getInt(Config.GRID_SIZE);
		int height = this.config.getInt(Config.GRID_HEIGHT);
		
		if (!this.config.getBoolean(Config.CLASSIC_MODE)){
			return new PlotsGenerator(size, height);
		}else{
			return new PlotsGenerator(size, height, baseId, surfaceId, pathId, wallLowerId, wallUpperId, plotBiome, pathBiome);
		}
	}
	
	public static InfinitePlots getInstance(){
		return instance;
	}
	
	public File getPlotsDir(){
		return this.plotsDir;
	}
	
	public PlotManager getPlotManager(){
		return this.plotManager;
	}
	
}