package com.lauren.simplenews.news.postbeans;

import java.util.List;

/**
 * Created by LGQ on 2017/11/24.
 */

public class Root {
    private List<Top> top;

    private List<NBA> NBA;

    private List<Cars> cars;

    private List<Jokes> jokes;

    public void setTop(List<Top> top){
        this.top = top;
    }
    public List<Top> getTop(){
        return this.top;
    }
    public void setNBA(List<NBA> NBA){
        this.NBA = NBA;
    }
    public List<NBA> getNBA(){
        return this.NBA;
    }
    public void setCars(List<Cars> cars){
        this.cars = cars;
    }
    public List<Cars> getCars(){
        return this.cars;
    }
    public void setJokes(List<Jokes> jokes){
        this.jokes = jokes;
    }
    public List<Jokes> getJokes(){
        return this.jokes;
    }
}
