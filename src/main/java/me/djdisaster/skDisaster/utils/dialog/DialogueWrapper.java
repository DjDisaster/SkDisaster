package me.djdisaster.skDisaster.utils.dialog;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.ServerLinksDialog;
import net.md_5.bungee.api.dialog.action.ActionButton;
import net.md_5.bungee.api.dialog.action.StaticAction;

public class DialogueWrapper {


    public DialogueWrapper() {

        ActionButton website = new ActionButton(
                new TextComponent("Visit Website"),
                new StaticAction(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/yourInvite"))
        );

    }

}
