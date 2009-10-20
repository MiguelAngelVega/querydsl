/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.OComparable;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.OString;
import com.mysema.query.types.operation.Ops;


/**
 * EString represents String expressions
 * 
 * @author tiwe
 * @see java.lang.String
 */
@SuppressWarnings("serial")
public abstract class EString extends EComparable<String> {
    
    private static final Map<String,EString> cache;
    
    static{
        List<String> strs = new ArrayList<String>();
        strs.addAll(Arrays.asList("", ".", ".*", "%", "id", "name"));
        for (int i = 0; i < 256; i++){
            strs.add(String.valueOf(i));
        }
    
        cache = new HashMap<String,EString>(strs.size());
        for (String str : strs){
            cache.put(str, new EStringConst(str));
        }
    }
    
    
    /**
     * Factory method for constants
     * 
     * @param str
     * @return
     */
    public static final EString create(String str){
        if (cache.containsKey(str)){
            return cache.get(str);
        }else{
            return new EStringConst(Assert.notNull(str));
        }        
    }
    
    private volatile ENumber<Long> length;
    
    private volatile EString lower, trim, upper;

    public EString() {
        super(String.class);
    }

    /**
     * @param str
     * @return this + str
     */
    public EString append(Expr<String> str) {
        return OString.create(Ops.CONCAT, this, str);
    }

    /**
     * @param str
     * @return this + str
     */
    public EString append(String str) {
        return append(EString.create(str));
    }
    
    /**
     * @param i
     * @return this.charAt(i)
     * @see java.lang.String#charAt(int)
     */
    public Expr<Character> charAt(Expr<Integer> i) {
        return OComparable.create(Character.class, Ops.CHAR_AT, this, i);
    }

    /**
     * @param i
     * @return this.charAt(i)
     * @see java.lang.String#charAt(int)
     */
    public Expr<Character> charAt(int i) {
        return charAt(ENumber.create(i));
    }

    /**
     * @param str
     * @return this + str
     */
    public EString concat(Expr<String> str) {
        return append(str);
    }

    /**
     * @param str
     * @return this + str
     */
    public EString concat(String str) {
        return append(str);
    }

    /**
     * @param str
     * @return this.contains(str)
     * @see java.lang.String#contains(CharSequence)
     */
    public EBoolean contains(Expr<String> str) {
        return OBoolean.create(Ops.STRING_CONTAINS, this, str);
    }

    /**
     * @param str
     * @return this.contains(str)
     * @see java.lang.String#contains(CharSequence)
     */
    public EBoolean contains(String str) {
        return contains(EString.create(str));
    }

    /**
     * @param str
     * @return this.endsWith(str)
     * @see java.lang.String#endsWith(String)
     */
    public EBoolean endsWith(Expr<String> str) {
        return OBoolean.create(Ops.ENDS_WITH, this, str);
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     * @see java.lang.String#endsWith(String)
     */
    public EBoolean endsWith(Expr<String> str, boolean caseSensitive) {
        if (caseSensitive){
            return endsWith(str);
        }else{
            return OBoolean.create(Ops.ENDS_WITH_IC, this, str);
        }        
    }

    /**
     * @param str
     * @return this.endsWith(str)
     * @see java.lang.String#endsWith(String)
     */
    public EBoolean endsWith(String str) {
        return endsWith(EString.create(str));
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     * @see java.lang.String#endsWith(String)
     */
    public EBoolean endsWith(String str, boolean caseSensitive) {
        return endsWith(EString.create(str), caseSensitive);
    }

    /**
     * @param str
     * @return this.equalsIgnoreCase(str)
     * @see java.lang.String#equalsIgnoreCase(String)
     */
    public EBoolean equalsIgnoreCase(Expr<String> str) {
        return OBoolean.create(Ops.EQ_IGNORE_CASE, this, str);
    }

    /**
     * @param str
     * @return this.equalsIgnoreCase(str)
     * @see java.lang.String#equalsIgnoreCase(String)
     */
    public EBoolean equalsIgnoreCase(String str) {
        return equalsIgnoreCase(EString.create(str));
    }

    /**
     * @param str
     * @return this.indexOf(str)
     * @see java.lang.String#indexOf(String)
     */
    public ENumber<Integer> indexOf(Expr<String> str) {
        return ONumber.create(Integer.class, Ops.INDEX_OF, this, str);
    }

    /**
     * @param str
     * @return this.indexOf(str)
     * @see java.lang.String#indexOf(String)
     */
    public ENumber<Integer> indexOf(String str) {
        return indexOf(EString.create(str));
    }

    /**
     * @param str
     * @param i
     * @return this.indexOf(str, i)
     * @see java.lang.String#indexOf(String, int)
     */
    public ENumber<Integer> indexOf(String str, int i) {
        return indexOf(EString.create(str), i);
    }

    
    /**
     * @param str
     * @param i
     * @return
     */
    public ENumber<Integer> indexOf(Expr<String> str, int i) {
        return ONumber.create(Integer.class, Ops.INDEX_OF_2ARGS, this, str, ENumber.create(i));
    }
    /**
     * @return this.isEmpty()
     * @see java.lang.String#isEmpty()
     */
    public EBoolean isEmpty(){
        return OBoolean.create(Ops.STRING_IS_EMPTY, this);
    }

    /**
     * @return !this.isEmpty()
     * @see java.lang.String#isEmpty()
     */
    public EBoolean isNotEmpty(){
        return isEmpty().not();
    }


    /**
     * @return this.length()
     * @see java.lang.String#length()
     */
    public ENumber<Long> length() {
        if (length == null) {
            length = ONumber.create(Long.class, Ops.STRING_LENGTH, this);
        }
        return length;
    }
    
    /**
     * Expr: <code>this like str</code>
     * 
     * @param str
     * @return
     */
    public EBoolean like(String str){
        return OBoolean.create(Ops.LIKE, this, EString.create(str));
    }
    
    /**
     * Expr: <code>this like str</code>
     * 
     * @param str
     * @return
     */
    public EBoolean like(EString str){
        return OBoolean.create(Ops.LIKE, this, str);
    }
    
    /**
     * 
     * @return this.toLowerCase()
     * @see java.lang.String#toLowerCase()
     */
    public EString lower() {
        if (lower == null) {
            lower = OString.create(Ops.LOWER, this);
        }
        return lower;
    }

    /**
     * @param regex
     * @return this.matches(right)
     * @see java.lang.String#matches(String)
     */
    public EBoolean matches(Expr<String> regex){
        return OBoolean.create(Ops.MATCHES, this, regex);
    }

    /**
     * @param regex
     * @return this.matches(regex)
     * @see java.lang.String#matches(String)
     */
    public EBoolean matches(String regex){
        return matches(EString.create(regex));
    }

    /**
     * @param str
     * @return str + this
     */
    public EString prepend(Expr<String> str) {
        return OString.create(Ops.CONCAT, str, this);
    }
    
    /**
     * @param str
     * @return str + this
     */
    public EString prepend(String str) {
        return prepend(EString.create(str));
    }
    
    /**
     * Split the given String with regex as the matcher for the separator
     * 
     * @param regex
     * @return this.split(regex)
     * @see java.lang.String#split(String)
     */
    public Expr<String[]> split(String regex) {
        return OSimple.create(String[].class, Ops.StringOps.SPLIT, this, EString.create(regex));
    }
    
    /**
     * @param str
     * @return this.startsWith(str)
     * @see java.lang.String#startsWith(String)
     */
    public EBoolean startsWith(Expr<String> str) {
        return OBoolean.create(Ops.STARTS_WITH, this, str);
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     * @see java.lang.String#startsWith(String)
     */
    public EBoolean startsWith(Expr<String> str, boolean caseSensitive) {
        if (caseSensitive){
            return startsWith(str);
        }else{
            return OBoolean.create(Ops.STARTS_WITH_IC, this, str);
        }  
    }

    /**
     * @param str
     * @return this.startsWith(str)
     * @see java.lang.String#startsWith(String)
     */
    public EBoolean startsWith(String str) {
        return startsWith(EString.create(str));
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     * @see java.lang.String#startsWith(String)
     */
    public EBoolean startsWith(String str, boolean caseSensitive) {
        return startsWith(EString.create(str), caseSensitive);
    }

    /* (non-Javadoc)
     * @see com.mysema.query.types.expr.EComparable#stringValue()
     */
    public EString stringValue() {
        return this;
    }

    /**
     * @param beginIndex
     * @return this.substring(beginIndex)
     * @see java.lang.String#substring(int)
     */
    public EString substring(int beginIndex) {
        return OString.create(Ops.SUBSTR_1ARG, this, ENumber.create(beginIndex));
    }

    /**
     * @param beginIndex
     * @param endIndex
     * @return this.substring(beginIndex, endIndex)
     * @see java.lang.String#substring(int, int)
     */
    public EString substring(int beginIndex, int endIndex) {
        return OString.create(Ops.SUBSTR_2ARGS, this, ENumber.create(beginIndex), ENumber.create(endIndex));
    }

    /**
     * 
     * @return this.toLowerCase()
     * @see java.lang.String#toLowerCase()
     */
    public EString toLowerCase() {
        return lower();
    }

    /**
     * 
     * @return
     * @see java.lang.String#toUpperCase()
     */
    public EString toUpperCase() {
        return upper();
    }
    
    /**
     * @return
     * @see java.lang.String#trim()
     */
    public EString trim() {
        if (trim == null) {
            trim = OString.create(Ops.TRIM, this);
        }
        return trim;
    }
    
    /**
     * @return
     * @see java.lang.String#toUpperCase()
     */
    public EString upper() {
        if (upper == null) {
            upper = OString.create(Ops.UPPER, this);
        }
        return upper;
    }
    
}