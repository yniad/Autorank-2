package me.armar.plugins.autorank.pathbuilder.requirement;

import me.armar.plugins.autorank.language.Lang;
import me.staartvin.plugins.pluginlibrary.Library;
import me.staartvin.plugins.pluginlibrary.hooks.BattleLevelsHook;

import java.util.UUID;

public class BattleLevelsKillsRequirement extends AbstractRequirement {

    private BattleLevelsHook handler = null;
    private int numberOfKills = -1;

    @Override
    public String getDescription() {

        return Lang.BATTLELEVELS_KILLS_REQUIREMENT.getConfigValue(numberOfKills);
    }

    @Override
    public String getProgressString(UUID uuid) {
        return handler.getKills(uuid) + "/" + numberOfKills;
    }

    @Override
    protected boolean meetsRequirement(UUID uuid) {

        if (!handler.isAvailable())
            return false;

        return handler.getKills(uuid) >= numberOfKills;
    }

    @Override
    public boolean initRequirement(final String[] options) {

        // Add dependency
        addDependency(Library.BATTLELEVELS);

        handler = (BattleLevelsHook) this.getDependencyManager().getLibraryHook(Library.BATTLELEVELS);

        if (options.length > 0) {
            try {
                numberOfKills = Integer.parseInt(options[0]);
            } catch (NumberFormatException e) {
                this.registerWarningMessage("An invalid number is provided");
                return false;
            }
        }

        if (numberOfKills < 0) {
            this.registerWarningMessage("No number is provided or smaller than 0.");
            return false;
        }

        return true;
    }

    @Override
    public boolean needsOnlinePlayer() {
        return false;
    }
}
