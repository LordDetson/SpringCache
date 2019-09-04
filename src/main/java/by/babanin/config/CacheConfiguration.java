package by.babanin.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/*
SimpleCacheManager — самый простой кэш-менеджер, удобный для изучения и тестирования.
ConcurrentMapCacheManager — лениво инициализирует возвращаемые экземпляры для каждого запроса. Также рекомендуется для тестирования и изучения работы с кэшем, а также, для каких-то простых действий, вроде наших. Для серьёзной работы с кэшем рекомендуются имплементации ниже.
JCacheCacheManager, EhCacheCacheManager, CaffeineCacheManager — серьёзные кэш-менеджеры «от партнёров», гибко настраиваемые и выполняющие задачи очень широкого спектра действия.

При создании кэш-менеджера переопределим метод createConcurrentMapCache, в котором вызовем CacheBuilder от Guava. В процессе нам будет предложено настроить кэш-менеджер при помощи инициализации следующих методов:

maximumSize — максимальный размер значений, которые может содержать кэш. При помощи этого параметра можно найти попытаться найти компромисс между нагрузкой на базу данных и на оперативную память JVM.
refreshAfterWrite — время после записи значения в кэш, после которого оно автоматически обновится.
expireAfterAccess — время жизни значения после последнего обращения к нему.
expireAfterWrite — время жизни значения после записи в кэш. Именно этот параметр мы определим.

и прочих.
 */

@Configuration
@EnableCaching
public class CacheConfiguration {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(1, TimeUnit.SECONDS)
                                .build().asMap(),
                        false);
            }
        };
    }
}
