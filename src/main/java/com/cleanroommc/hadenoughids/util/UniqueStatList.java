package com.cleanroommc.hadenoughids.util;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.item.Item;
import net.minecraft.stats.StatCrafting;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class UniqueStatList extends ArrayList<StatCrafting> {

    private final Reference2ObjectMap<Item, StatCrafting> backingMap;

    public UniqueStatList() {
        this.backingMap = new Reference2ObjectOpenHashMap<>();
    }

    public Reference2ObjectMap<Item, StatCrafting> getBackingMap() {
        return backingMap;
    }

    public StatCrafting getOrPut(Item key, Supplier<StatCrafting> value) {
        StatCrafting stat = this.backingMap.get(key);
        if (stat == null) {
            StatCrafting got = value.get();
            got.registerStat();
            this.backingMap.put(key, stat = got);
        }
        return stat;
    }

    @Override
    public StatCrafting set(int index, StatCrafting element) {
        StatCrafting previous = super.set(index, element);
        if (previous != null) {
            this.backingMap.remove(previous.getItem());
        }
        this.backingMap.put(element.getItem(), element);
        return previous;
    }

    @Override
    public boolean add(StatCrafting statCrafting) {
        if (!this.backingMap.containsKey(statCrafting.getItem())) {
            this.backingMap.put(statCrafting.getItem(), statCrafting);
        }
        return super.add(statCrafting);
    }

    @Override
    public void add(int index, StatCrafting element) {
        if (!this.backingMap.containsKey(element.getItem())) {
            this.backingMap.put(element.getItem(), element);
        }
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends StatCrafting> c) {
        boolean changed = super.addAll(c);
        if (changed) {
            c.forEach(s -> {
                if (!this.backingMap.containsKey(s.getItem())) {
                    this.backingMap.put(s.getItem(), s);
                }
            });
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends StatCrafting> c) {
        boolean changed = super.addAll(index, c);
        if (changed) {
            c.forEach(s -> {
                if (!this.backingMap.containsKey(s.getItem())) {
                    this.backingMap.put(s.getItem(), s);
                }
            });
        }
        return changed;
    }

    @Override
    public StatCrafting remove(int index) {
        StatCrafting removed = super.remove(index);
        this.backingMap.remove(removed.getItem());
        return removed;
    }

    @Override
    public boolean remove(Object o) {
        if (super.remove(o)) {
            this.backingMap.values().remove(o);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        super.clear();
        this.backingMap.clear();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (this.backingMap.values().removeAll(c)) {
            return super.removeAll(c);
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (this.backingMap.values().retainAll(c)) {
            return super.retainAll(c);
        }
        return false;
    }

    @Override
    public void replaceAll(UnaryOperator<StatCrafting> operator) {
        super.replaceAll(operator);
        this.backingMap.replaceAll((k, v) -> operator.apply(v));
    }

    @Override
    public boolean removeIf(Predicate<? super StatCrafting> filter) {
        if (this.backingMap.values().removeIf(filter)) {
            return super.removeIf(filter);
        }
        return false;
    }

    @Override
    public ListIterator<StatCrafting> listIterator(int index) {
        return super.listIterator(index);
    }

    @Override
    public ListIterator<StatCrafting> listIterator() {
        return super.listIterator();
    }

    @Override
    public Iterator<StatCrafting> iterator() {
        return super.iterator();
    }

}
