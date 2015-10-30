package com.quickship.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quickship.entity.Area;
import com.quickship.service.AreaService;

@Controller
@RequestMapping("/area")
public class AreaController extends BaseController {
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@ResponseBody
	@RequestMapping(value = "/areaList", method = RequestMethod.GET)
	public List<Area> addShipping(Long areaId) {
		Area area = areaService.find(areaId);
		if (area != null) {
			return areaService.findChild(area);
		} else {
			return null;
		}
	}
}
