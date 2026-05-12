package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new Song[0];
    }

    public String getName() {
        return name;
    }

    public void addSong(Song song) {
        Song[] copy = new Song[songs.length + 1];
        System.arraycopy(songs, 0, copy, 0, songs.length);
        copy[copy.length - 1] = song;
        songs = copy;
    }

    public void printSortedByTitle() {
        Song[] copy = songs.clone();
        Arrays.sort(copy);
        printSongs(copy);
    }

    public void printSortedByDuration() {
        Song[] copy = songs.clone();
        Arrays.sort(copy, new SongDurationComparator());
        printSongs(copy);
    }

    public int getTotalDuration() {
        int total = 0;
        for (Song song : songs) {
            total += song.durationSeconds();
        }
        return total;
    }

    private void printSongs(Song[] songsToPrint) {
        for (Song song : songsToPrint) {
            System.out.println(song);
        }
    }
}
