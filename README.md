#Опис на проектот

Целта на овој проект е да се развие напредна апликација која ќе овозможи обработка на историски податоци за акции од Македонската берза. Апликацијата ќе се базира на архитектурата Pipeline and Filter, што ќе овозможи модуларен и скалабилен пристап за обработка на податоците. Системот ќе автоматизира процесот на преземање, трансформација и чистење на дневните берзиски податоци за сите компании на македонската берза во период од последните 10 години. Ова вклучува преземање на податоци од надворешни извори, филтрирање на релевантни информации како што се датум, цени на отворање и затворање, обем на тргување и високи/ниски вредности, и нивно форматирање за складирање во база на податоци.

Излезот на апликацијата ќе биде целосно структуриран и чист податочен базен, подготвен за понатамошна анализа. Идејата е овој првичен систем да постави основа за развој на апликации за анализа на берзата со целосни карактеристики во идните фази. Системот ќе обезбеди висок квалитет и конзистентност на податоците, што ќе биде клучно за понатамошни анализа и донесување одлуки.

Апликацијата ќе биде дизајнирана со низа филтри кои ќе се применуваат во континуиран процес во pipeline-архитектурата. Овој пристап овозможува модуларност и флексибилност во обработката, каде секој филтер ќе извршува конкретна задача, а излезот од секој филтер ќе биде пренесен како влез во следниот. Филтрите ќе бидат одговорни за различни фази на обработка — од почетно пребарување и екстракција на податоци, до нивна финална трансформација и форматирање за складирање. Оваа архитектура ќе послужи како основа за идни проширувања на апликацијата, како што се интеграција на напредни аналитички алатки или дополнителни функционалности базирани на различни архитектурни парадигми.

Изработено од:

Атанас Деспотовски 221245
Филип Лозаноски 221041
Мартин Видевски 221035
