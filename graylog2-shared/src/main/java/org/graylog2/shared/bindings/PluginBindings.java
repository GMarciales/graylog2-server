/**
 * This file is part of Graylog2.
 *
 * Graylog2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog2.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graylog2.shared.bindings;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.graylog2.plugin.Plugin;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Set;

public class PluginBindings extends AbstractModule {
    private final Set<Plugin> plugins;

    public PluginBindings(Set<Plugin> plugins) {
        this.plugins = plugins;
    }

    @Override
    protected void configure() {
        final Multibinder<Plugin> pluginbinder = Multibinder.newSetBinder(binder(), Plugin.class);
        final Multibinder<PluginMetaData> pluginMetaDataBinder = Multibinder.newSetBinder(binder(), PluginMetaData.class);

        for (final Plugin plugin : plugins) {
            pluginbinder.addBinding().toInstance(plugin);
            for (final PluginModule pluginModule : plugin.modules()) {
                binder().install(pluginModule);
            }

            pluginMetaDataBinder.addBinding().toInstance(plugin.metadata());
        }
    }
}
