/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.index.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.hunspell.HunspellDictionary;
import org.apache.lucene.analysis.hunspell.HunspellStemFilter;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

/**
 * @author Jörg Prante
 */
public class HunspellStemFilterFactory extends AbstractTokenFilterFactory {

    private final String name;
    private final String locale;
    private final boolean ignoreCase;
    private final boolean dedup;
    private HunspellDictionary dictionary;
    private final static Set<String> locales = new HashSet(Arrays.asList(
            "bg_BG", "da_DK", "el_GR", "en_GB", "fr_FR", "hr_HR", "it_IT", "nb_NO",
            "pt_BR", "ru_RU", "sl_SI", "uk_UA", "ca_ES", "de_DE", "en_AU", "en_US",
            "he_IL", "hu_HU", "lt_LT", "nl_NL", "pt_PT", "sh", "sr", "vi_VI",
            "cs_CZ", "de_DE_neu", "en_CA", "es_ES", "hi_IN", "id_ID", "lv_LV",
            "pl_PL", "ro_RO", "sk_SK", "sv_SE", "vi_VN"));

    @Inject
    public HunspellStemFilterFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        this.name = name;
        this.locale = settings.get("locale", "en_US");
        this.ignoreCase = settings.getAsBoolean("ignoreCase", Boolean.TRUE);
        this.dedup = settings.getAsBoolean("dedup", Boolean.TRUE);
        if (!locales.contains(locale)) {
            throw new ElasticSearchException("invalid locale '" + locale + "' for hunspell aff/dic");
        }
        try {
            InputStream affixStream = HunspellStemFilterFactory.class.getResourceAsStream(locale + ".aff");
            InputStream dictStream = HunspellStemFilterFactory.class.getResourceAsStream(locale + ".dic");
            this.dictionary = new HunspellDictionary(affixStream, dictStream, version, ignoreCase);
            affixStream.close();
            dictStream.close();
        } catch (IOException ex) {
            logger.error("hunspell aff/dic stream I/O error for locale " + locale, ex);
        } catch (ParseException ex) {
            logger.error("hunspell aff/dic stream parse failure for locale " + locale, ex);
        }
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new HunspellStemFilter(tokenStream, dictionary, dedup);
    }

    public static Set<String> getLocales() {
        return locales;
    }
}