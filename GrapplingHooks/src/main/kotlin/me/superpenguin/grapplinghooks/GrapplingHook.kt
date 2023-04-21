package me.superpenguin.grapplinghooks

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class GrapplingHook(plugin: GrapplingHooksMain): Listener, CommandExecutor {
    // Arbritrary value determining how much power the grappling hook has
    val POWER = 3

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        plugin.getCommand("grapplinghook")!!.setExecutor(this)
    }

    companion object {
        fun getItem(): ItemStack {
            val item = ItemStack(Material.FISHING_ROD)
            val meta = item.itemMeta!!

            // Customise your item here
            meta.setDisplayName("Grappling Hook")
            meta.setLocalizedName("GrapplingHook") // <-- Identifier for the item
            meta.addEnchant(Enchantment.DURABILITY, 1, false)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            meta.lore = listOf("This is a grappling hook")

            item.itemMeta = meta
            return item
        }

    }

    // Since spigot does not provide a way to check the rod in the players hand when they reel it in, we will have to figure it out ourselves
    val grappling: ArrayList<UUID> = ArrayList()

    // If you don't want a cooldown, you can just remove the 3 cooldown parts :) (1)
    val COOLDOWN_SECONDS = 3
    val onCooldown: Cache<UUID, Long> = CacheBuilder.newBuilder().expireAfterWrite(COOLDOWN_SECONDS.toLong(), TimeUnit.SECONDS).build()

    @EventHandler
    fun onFishingRodUse(event: PlayerFishEvent){
        val uuid = event.player.uniqueId
        // Called when they cast the fishing rod
        if (event.state == PlayerFishEvent.State.FISHING) {
            // Ensure they are using the grappling hook
            if (event.hand == null || event.player.inventory.getItem(event.hand!!)?.itemMeta?.localizedName?.equals("GrapplingHook") != true) return

            // Check if they are on cooldown and return if they are. (2)
            val timeUntilUse = onCooldown.getIfPresent(uuid).let { if (it == null) 0 else it - System.currentTimeMillis() }
            if (timeUntilUse > 0) {
                event.isCancelled = true
                return event.player.sendMessage("You cannot use this for another ${(timeUntilUse/1000.0).roundToInt()} seconds")
            }

            // Add them to the list so we know they're grappling
            grappling.add(uuid)

        // When they reel in a fishing rod, check that they are grappling.
        } else if (event.state == PlayerFishEvent.State.REEL_IN && uuid in grappling) {
            // Get a vector pointing from the player to the fishing hook and add it to the players existing velocity.
            val additionalVelocity = event.hook.location.subtract(event.player.location).toVector().normalize().multiply(POWER)
            event.player.velocity = event.player.velocity.add(additionalVelocity)
            grappling.remove(uuid)

            // Put the player on a cooldown (3)
            onCooldown.put(uuid, System.currentTimeMillis() + (COOLDOWN_SECONDS * 1000))
        } else if (uuid in grappling) {
            // they probably caught a fish, let's remove them from the list :)
            grappling.remove(uuid)
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // using a method with a "Unit" return type in kotlin may make your code cleaner
        execute(sender, command, args)
        return true
    }

    fun execute(sender: CommandSender, command: Command, args: Array<out String>){
        // When your function doesn't specify a return type you can use "return sender.sendMessage..."
        if (!sender.hasPermission("grapplinghook.give")) return sender.sendMessage("You do not have permission for this")
        if (sender !is Player) return sender.sendMessage("You must be a player")
        // You can specify more conditions here if you wish to extend the command to support giving it to other players.

        // Kotlin automatically casts sender to player because we checked it above.
        sender.inventory.addItem(getItem())
        sender.sendMessage("Recived a Grappling Hook!")
    }
}