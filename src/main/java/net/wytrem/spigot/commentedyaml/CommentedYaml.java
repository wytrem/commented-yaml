package net.wytrem.spigot.commentedyaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.serializer.Serializer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An hacky extension of {@link Yaml} that allows to write comments when dumping.
 */
public class CommentedYaml extends Yaml {

    private final CommentsProvider commentsProvider;

    public CommentedYaml(CommentsProvider commentsProvider) {
        this.commentsProvider = commentsProvider;
    }

    public CommentedYaml(DumperOptions dumperOptions, CommentsProvider commentsProvider) {
        super(dumperOptions);
        this.commentsProvider = commentsProvider;
    }

    public CommentedYaml(LoaderOptions loadingConfig, CommentsProvider commentsProvider) {
        super(loadingConfig);
        this.commentsProvider = commentsProvider;
    }

    public CommentedYaml(Representer representer, CommentsProvider commentsProvider) {
        super(representer);
        this.commentsProvider = commentsProvider;
    }

    public CommentedYaml(BaseConstructor constructor, CommentsProvider commentsProvider) {
        super(constructor);
        this.commentsProvider = commentsProvider;
    }

    public CommentedYaml(BaseConstructor constructor, Representer representer, CommentsProvider commentsProvider) {
        super(constructor, representer);
        this.commentsProvider = commentsProvider;
    }

    public CommentedYaml(Representer representer, DumperOptions dumperOptions, CommentsProvider commentsProvider) {
        super(representer, dumperOptions);
        this.commentsProvider = commentsProvider;
    }

    public CommentedYaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, CommentsProvider commentsProvider) {
        super(constructor, representer, dumperOptions);
        this.commentsProvider = commentsProvider;
    }

    public CommentedYaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, LoaderOptions loadingConfig, CommentsProvider commentsProvider) {
        super(constructor, representer, dumperOptions, loadingConfig);
        this.commentsProvider = commentsProvider;
    }

    public CommentedYaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, Resolver resolver, CommentsProvider commentsProvider) {
        super(constructor, representer, dumperOptions, resolver);
        this.commentsProvider = commentsProvider;
    }

    public CommentedYaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, LoaderOptions loadingConfig, Resolver resolver, CommentsProvider commentsProvider) {
        super(constructor, representer, dumperOptions, loadingConfig, resolver);
        this.commentsProvider = commentsProvider;
    }

    @Override
    public String dump(Object data) {
        List<Object> list = new ArrayList(1);
        list.add(data);
        return this.dumpAll(list.iterator());
    }

    @Override
    public void dump(Object data, Writer output) {
        List<Object> list = new ArrayList(1);
        list.add(data);
        this.dumpAll(list.iterator(), output, (Tag)null);
    }

    @Override
    public String dumpAll(Iterator<? extends Object> data) {
        StringWriter buffer = new StringWriter();
        this.dumpAll(data, buffer, (Tag)null);
        return buffer.toString();
    }

    @Override
    public void dumpAll(Iterator<? extends Object> data, Writer output) {
        this.dumpAll(data, output, (Tag)null);
    }

    private void dumpAll(Iterator<? extends Object> data, Writer output, Tag rootTag) {
        Serializer serializer = new Serializer(new CommentedEmitter(output, this.dumperOptions, this.commentsProvider), this.resolver, this.dumperOptions, rootTag);

        try {
            serializer.open();

            while(data.hasNext()) {
                Node node = this.representer.represent(data.next());
                serializer.serialize(node);
            }

            serializer.close();
        } catch (IOException var6) {
            throw new YAMLException(var6);
        }
    }
}
