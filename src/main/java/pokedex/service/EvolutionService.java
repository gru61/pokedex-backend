package pokedex.service;


import org.springframework.stereotype.Service;

/**
 * Service zur Prüfung der Entwicklungen der Pokemon
 */
@Service
public class EvolutionService {

    /**
     * Prüfung, ob eine Entwicklung von einer Pokedex-ID zur nächsten ID erlaubt ist
     *
     * @param currentSpeciesId Die aktuelle Pokedex-ID des Pokemons
     * @param targetSpeciesId Die neue Pokedex-ID nach der Entwicklung
     * @return true, wenn die entwicklung erlaubt ist
     */
    public boolean isAllowedEvolution(Integer currentSpeciesId, Integer targetSpeciesId) {
        if (currentSpeciesId == null || targetSpeciesId == null) {
            return false;
        }

        return switch (currentSpeciesId) {

            case 1 -> targetSpeciesId == 2;//bisasam-bisaknosp
            case 2 -> targetSpeciesId == 3;//bisaknosp-bisaflor


            case 4 -> targetSpeciesId == 5;//glumanda-glutexo
            case 5 -> targetSpeciesId == 6;//glutexo-glurak


            case 7 -> targetSpeciesId == 8;//schiggy-schillok
            case 8 -> targetSpeciesId == 9;//shchillok-turtok


            case 10 -> targetSpeciesId == 11;//raupy-safcon
            case 11 -> targetSpeciesId == 12;//safcon-smettbo


            case 13 -> targetSpeciesId == 14;//hornliu-kokuna
            case 14 -> targetSpeciesId == 15;//kokuna-bibor


            case 16 -> targetSpeciesId == 17;//taubsi-tauboga
            case 17 -> targetSpeciesId == 18;//tauboga-tauboss


            case 19 -> targetSpeciesId == 20;//rattfratz-rattikarl


            case 21 -> targetSpeciesId == 22;//habitak-ibitak


            case 23 -> targetSpeciesId == 24;//rettan-arbok


            case 25 ->  targetSpeciesId == 26;//pikachu-raichu


            case 27 -> targetSpeciesId == 28;//sandan-sandamer


            case 29 -> targetSpeciesId == 30;//nidoran(w)-nidorina
            case 30 -> targetSpeciesId == 31;//nidorina-nidoqueen


            case 32 -> targetSpeciesId == 33;//nidoran(m)-nidorino
            case 33 -> targetSpeciesId == 34;//nidorino-nidoking


            case 35 -> targetSpeciesId == 36;//piepi-pixi


            case 37 -> targetSpeciesId == 38;//vulpix-vulnona


            case 39 -> targetSpeciesId == 40;//pummeluff-knudeluff


            case 41 -> targetSpeciesId == 42;//zubat-golbat


            case 43 -> targetSpeciesId == 44;//myrapla-duflor
            case 44 -> targetSpeciesId == 45;//duflor-giflor


            case 46 -> targetSpeciesId == 47;//paras-parasek


            case 48 -> targetSpeciesId == 49;//bluzuk-omot


            case 50 -> targetSpeciesId == 51;//digda-digdri


            case 52 -> targetSpeciesId == 53;//mauzi-snobilikat


            case 54 -> targetSpeciesId == 55;//enton-enteron


            case 56 -> targetSpeciesId == 57;//menki-rasaff


            case 58 -> targetSpeciesId == 59;//fukano-arkani


            case 60 -> targetSpeciesId == 61;//quapsel-quaputzi
            case 61 -> targetSpeciesId == 62;//quaputzi-quappo


            case 63 -> targetSpeciesId == 64;//abra-kadabra
            case 64 -> targetSpeciesId == 65;//kadabra-simsala


            case 66 -> targetSpeciesId == 67;//machollo-maschock
            case 67 -> targetSpeciesId == 68;//maschock-machomei


            case 69 -> targetSpeciesId == 70;//knofensa-ultrigaria
            case 70 -> targetSpeciesId == 71;//ultrigaria-sarzenia


            case 72 -> targetSpeciesId == 73;//tantache-tentoxa


            case 74 -> targetSpeciesId == 75;//kleinstein-georok
            case 75 -> targetSpeciesId == 76;//georok-geowaz


            case 77 -> targetSpeciesId == 78;//ponita-galoppa


            case 79 -> targetSpeciesId == 80;//flegmon-lahmus


            case 81 -> targetSpeciesId == 82;//magnetilo-magneton


            //83 porenta hat keine entwicklung


            case 84 -> targetSpeciesId == 85;//dodu-dodri


            case 86 -> targetSpeciesId == 87;//jurob-jugong


            case 88 -> targetSpeciesId == 89;//sleima-sleimok


            case 90 -> targetSpeciesId == 91;//muschas-austos


            case 92 -> targetSpeciesId == 93;//nebulak-alpollo
            case 93 -> targetSpeciesId == 94;//alpollo-gengar


            //95 onix hat keine entwicklung


            case 96 -> targetSpeciesId == 97;//traumato-hypno


            case 98 -> targetSpeciesId == 99;//krabby-kingler


            case 100 -> targetSpeciesId == 101;//voltoball-lektroball


            case 102 -> targetSpeciesId == 103;//owei-kokowei


            case 104 -> targetSpeciesId == 105;//tragosso-knogga


            //106 kicklee hat keine entwicklung


            //107 nockchan hat keine entwicklung


            //108 schlurp hat keine entwicklung


            case 109 -> targetSpeciesId == 110;//smogon-smogsmog


            case 111 -> targetSpeciesId == 112;//rihorn-rizeros


            //113 chaneira hat keine entwicklung


            //114 tangela hat keine entwicklung


            //115 kangama hat keine entwicklung


            case 116 -> targetSpeciesId == 117;//seeper-seemon


            case 118 -> targetSpeciesId == 119;//goldini-golking


            case 120 -> targetSpeciesId == 121;//sterndu-starmie


            //122 pantimos hat keine entwicklung


            //123 sichlor hat keine entwicklung


            //124 rossana hat keine entwicklung


            //125 elektek hat keine entwicklung


            //126 magmar hat keine entwicklung


            //127 pinsir hat keine entwicklung


            //128 tauros hat keine entwicklung


            case 129 -> targetSpeciesId == 130;//karpador-garados


            //131 lapras hat keine entwicklung


            //132 ditto hat keine entwicklung


            case 133 -> targetSpeciesId == 134 || targetSpeciesId == 135 || targetSpeciesId == 136;//evoli-aquana | blitza | flamara


            //137 porygon hat keine entwicklung


            case 138 -> targetSpeciesId == 139;//amonitas-amoroso


            case 140 -> targetSpeciesId == 141;//kabuto-kabutops


            //142 aerodactyl hat keine entwicklung


            //143 relaxo hat keine entwickliung


            //144 arktos hat keine entwicklung


            //145 zapdos hat keine entwicklung


            //146 lavados hat keine entwicklung


            case 147 -> targetSpeciesId == 148;//dratini-dragonir
            case 148 -> targetSpeciesId == 149;//dragonir-dragoran


            //150 mewtu hat keine entwicklung


            //151 mew hat keine entwicklung


            default -> false;
        };
    }
}

