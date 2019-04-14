package co.lotc.core.bukkit.command;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import co.lotc.core.agnostic.Sender;
import co.lotc.core.bukkit.wrapper.BukkitSender;
import co.lotc.core.command.CmdArg;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArcheSuggestionProvider<T> implements SuggestionProvider<T> {
	private final CmdArg<T> arg;

	@Override
	public CompletableFuture<Suggestions> getSuggestions(CommandContext<T> context, SuggestionsBuilder builder) throws CommandSyntaxException {
		T source = context.getSource();
		Sender sender = new BukkitSender(BrigadierProvider.get().getBukkitSender(source));
		
		for(String sugg : arg.getCompleter().suggest(sender, builder.getRemaining())) {
			if (sugg.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
				builder.suggest(sugg);
			}
		}

		return builder.buildFuture();
	}

}
