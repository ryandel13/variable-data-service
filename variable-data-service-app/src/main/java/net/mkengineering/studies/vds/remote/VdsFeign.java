package net.mkengineering.studies.vds.remote;

import org.springframework.cloud.netflix.feign.FeignClient;

import net.mkengineering.studies.vds.service.ServiceInterface;

@FeignClient(name="variable-data-service", url="ryandel.selfhost.me:8801")
public interface VdsFeign extends ServiceInterface {

}
