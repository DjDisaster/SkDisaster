package me.djdisaster.skDisaster.elements.types;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import me.djdisaster.skDisaster.utils.reactivity.Binding;
import me.djdisaster.skDisaster.utils.reactivity.BindingSkriptListener;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.converter.Converters;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

public class TypeReactiveVar {

    static {


        Classes.registerClass(
                new ClassInfo<>(BindingSkriptListener.class, "bindinglistener")
                        .user("bindinglistener")
                        .name("Binding Listener")
                        .description("Listener on SkDisaster reactive var.")
                        .examples("")
                        .serializer(new Serializer<>() {
                            @Override
                            public Fields serialize(BindingSkriptListener listener) {
                                Fields f = new Fields();
                                f.putObject("functionName", listener.getFunctionName());
                                f.putObject("id", listener.getId());
                                return f;
                            }

                            @Override
                            public void deserialize(BindingSkriptListener listener, Fields fields) {
                            }

                            @Override
                            protected BindingSkriptListener deserialize(Fields fields) throws StreamCorruptedException {
                                String functionName = (String) fields.getObject("functionName");
                                String id = (String) fields.getObject("id");
                                return new BindingSkriptListener(functionName, id);
                            }

                            @Override
                            public boolean mustSyncDeserialization() {
                                return false;
                            }

                            @Override
                            public boolean canBeInstantiated() {
                                return false;
                            }
                        })
        );

        Classes.registerClass(
                new ClassInfo<>(Binding.class, "binding")
                        .user("binding_user")
                        .name("Binding")
                        .description("SkDisaster reactive var.")
                        .examples("")
                        .parser(new Parser<Binding>() {
                            @Override
                            public @Nullable Binding parse(String input, ParseContext context) {
                                return null;
                            }

                            @Override
                            public String toString(Binding binding, int flags) {
                                Object value = binding.getValue();
                                return value != null ? value.toString() : "null";
                            }

                            @Override
                            public String toVariableNameString(Binding binding) {
                                Object value = binding.getValue();
                                return value != null ? value.toString() : "null";
                            }
                        })
                        .changer(new Changer<Binding>() {
                            @Override
                            public Class<?>[] acceptChange(ChangeMode mode) {
                                return new Class[]{Object.class};
                            }

                            @Override
                            public void change(Binding[] bindings, @Nullable Object[] delta, ChangeMode mode) {
                                if (bindings == null || bindings.length == 0) return;

                                for (Binding b : bindings) {
                                    if (b == null) continue;

                                    Object current = b.getValue();
                                    Object newVal = (delta != null && delta.length > 0) ? delta[0] : null;

                                    switch (mode) {
                                        case SET -> b.setValue(newVal);
                                        case ADD -> {
                                            if (current instanceof Number && newVal instanceof Number) {
                                                double result = ((Number) current).doubleValue() + ((Number) newVal).doubleValue();
                                                b.setValue(result);
                                            }
                                        }
                                        case REMOVE -> {
                                            if (current instanceof Number && newVal instanceof Number) {
                                                double result = ((Number) current).doubleValue() - ((Number) newVal).doubleValue();
                                                b.setValue(result);
                                            }
                                        }
                                        default -> {
                                        }
                                    }
                                }
                            }
                        })
                        .serializer(new Serializer<>() {
                            @Override
                            public Fields serialize(Binding binding) {
                                Fields f = new Fields();

                                Object value = binding.getValue();
                                if (value != null)
                                    f.putObject("value", value);

                                if (binding.getListeners() != null && !binding.getListeners().isEmpty())
                                    f.putObject("listeners", binding.getListeners());

                                return f;
                            }

                            @Override
                            public void deserialize(Binding binding, Fields fields) {
                            }

                            @Override
                            protected Binding deserialize(Fields fields)
                                    throws StreamCorruptedException {

                                Object value = fields.getObject("value");
                                Binding newBinding = new Binding(value);

                                Object listenersObj = fields.getObject("listeners");
                                if (listenersObj instanceof java.util.List<?> list) {
                                    for (Object obj : list) {
                                        if (obj instanceof BindingSkriptListener listener) {
                                            newBinding.addListener(listener);
                                        }
                                    }
                                }

                                return newBinding;
                            }

                            @Override
                            public boolean mustSyncDeserialization() {
                                return false;
                            }

                            @Override
                            public boolean canBeInstantiated() {
                                return false;
                            }
                        })

        );

        Converters.registerConverter(Binding.class, Object.class, Binding::getValue);
    }
}
