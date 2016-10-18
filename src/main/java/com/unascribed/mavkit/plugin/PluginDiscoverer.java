package com.unascribed.mavkit.plugin;

import java.util.List;

import com.unascribed.mavkit.Mavkit;

public interface PluginDiscoverer {
	List<Plugin> discover(Mavkit mav);
}
