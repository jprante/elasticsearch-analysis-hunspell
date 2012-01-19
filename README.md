Hunspell Analysis for ElasticSearch
===================================

The Hunspell Analysis plugin integrates Lucene Hunspell module into ElasticSearch, adding Hunspell relates analysis components.

In order to install the plugin, simply run: `bin/plugin -install jprante/elasticsearch-analysis-hunspell`. 

    ---------------------------------------------
    | Hunspell Analysis Plugin | ElasticSearch  |
    ---------------------------------------------
    | master                   | 0.18           |
    ---------------------------------------------
    | 1.1.0                    | 0.18.7         |
    ---------------------------------------------
    | 1.0.0                    | < 0.18.7       |
    ---------------------------------------------


Example usage:

	index:
	  analysis:
	    filter:
	      hunspell_de:
	        type: hunspell
	        locale: de_DE
	        ignoreCase: true


Supported locales:

bg_BG
ca_ES
cs_CZ
da_DK
de_DE
de_DE_neu
el_GR
en_AU
en_CA
en_GB
en_US
es_ES
fr_FR
he_IL
hi_IN
hr_HR
hu_HU
id_ID
it_IT
lt_LT
lv_LV
nb_NO
nl_NL
pl_PL
pt_BR
pt_PT
ro_RO
ru_RU
sh
sk_SK
sl_SI
sr
sv_SE
uk_UA
vi_VI
vi_VN
