# sprbot-freedom

## 依赖版本清单
| 序号	| 依赖名称	| 版本号|
|--- |--- |--- |
|1 | JDK | 17
|2 | Spring Boot | 3.1.9
|3 | Mybatis-Plus | 3.5.5
|4 | Druid | 1.1.23
|5 | jjwt | 0.9.1
|6 | Mysql-driver | 8.0.23
|7 | fastdfs | 1.26.3
|8 | Mysql | 5.7
|9 | Redis | 5.0
|10 | Mongodb | 7.0.0
|11 | Netty | 5.0.0.Alpha2
|12 | Undertow | 2.3.12.Final
|13 | Nginx | 1.15.3

## 目录结构
![image](https://github.com/univspeed/sprbot-freedom/assets/23521638/84b25c8f-c1e3-4ecc-96d5-7b90efe92fc1)

## 权限校验
权限校验分为数据权限校验以及功能权限校验，权限通过前端进行配置，内容涵盖了接口、按钮、tab页、菜单权限，并与角色进行绑定，用户进行多角色关联，登陆后即可通过pipeline中的interfacePermFilterHandler和ResourcePermFilterHandler进行校验，完整流程如下所示：

![image](https://github.com/univspeed/sprbot-freedom/assets/23521638/9b62f5ad-c9da-4c41-8af7-b0c4b771f62a)

## 业务代码使用案例
以用户管理为例：
### SysUserController.java
```
/**
 * 操作员接口
 * @author liuyutang
 * @date 2023/7/11
 */
@PrintFunctionLog
@RestController
@RequestMapping("/api/v1/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("page")
    public PageResult<SysUserEntity> page(){

        return sysUserService.page(
                getPara2Int(DEFAULT_PAGE_NAME,DEFAULT_PAGE),
                getPara2Int(DEFAULT_PAGE_SIZE_NAME,DEFAULT_PAGE_SIZE),
                getPara("name")
        );
    }

    @GetMapping("list")
    public List<SysUserEntity> list(){

        return sysUserService.findAll(getPara("name"));
    }

    @GetMapping("info/{id}")
    public SysUserEntity info(@PathVariable("id") Long id){
        Assert.notNull(id, "查询详情id不能为空");
        return sysUserService.findById(id);
    }

    @PostMapping("save")
    public SysUserEntity save(@RequestBody SaveUserBO user){
        Assert.notNull(user, "新增操作员对象不能为空");
        return sysUserService.add(user);
    }

    @PostMapping("update")
    public SysUserEntity update(@RequestBody SaveUserBO user){
        Assert.notNull(user, "修改操作员对象不能为空");
        Long id = user.getId();
        Assert.notNull(id, "修改操作员对象ID不能为空");
        return sysUserService.update(user);
    }

    @PostMapping("del")
    public boolean del(@RequestBody IdBO idBO){
        Long id = idBO.getId();
        Assert.notNull(id,"删除操作员时ID不能为空");
        return sysUserService.del(id);
    }

    @PostMapping("modify_pass")
    public boolean modifyPass(@RequestBody ModifyPassBO modifyPassBO){
        Assert.notNull(modifyPassBO.getId(),"操作员ID不能为空");
        Assert.notNull(modifyPassBO.getOldPass(),"原密码不能为空");
        Assert.notNull(modifyPassBO.getNewPass(),"新密码不能为空");
        Assert.notNull(modifyPassBO.getComfirmPass(),"确认密码不能为空");
        return sysUserService.modifyPassword(modifyPassBO);
    }
```

### SysUserService.java
```
/**
 * 操作员业务层接口
 * @author liuyutang
 * @date 2023/7/11
 */
public interface SysUserService extends BaseService<SysUserEntity> {
    /**
     * 分页列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    PageResult<SysUserEntity> page(int page ,int pageSize,String name);

    /**
     * 用户列表
     * @param name
     * @return
     */
    List<SysUserEntity> findAll(String name);

    /**
     * 详情
     * @param id
     * @return
     */
    SysUserEntity findById(Long id);

    /**
     * 创建用户
     * @param user
     * @return
     */
    SysUserEntity add(SaveUserBO user);

    /**
     * 修改用户
     * @param old
     * @return
     */
    SysUserEntity update(SaveUserBO old);

    /**
     * 删除用户
     * @param id
     * @return
     */
    boolean del(Long id);

    /**
     * 修改密码
     * @param modifyPassBO
     * @return
     */
    boolean modifyPassword(ModifyPassBO modifyPassBO);

    /**
     * 更新最后一次登录时间
     * @param userId
     * @param time
     * @return
     */
    boolean modifyLastLoginTime(Long userId,String time);

    /**
     * 根据用户名查找
     * @param username
     * @return
     */
    SysUserEntity findByUserName(String username);
}
```

### SysUserServiceImpl.java
```
/**
 * 操作员业务层接口实现
 * @author liuyutang
 * @date 2023/7/11
 */
@Service
@PrintFunctionLog
@DB1DataSource
public class SysUserServiceImpl extends BaseServiceImpl<SysUserEntity, SysUserDAO> implements SysUserService {

    @Value("${system.sm2.encrypt-private-key}")
    private String privateKey;
    private final SysRoleUserService roleUserService;

    public SysUserServiceImpl(SysRoleUserService roleUserService) {
        this.roleUserService = roleUserService;
    }

    @Override
    public PageResult<SysUserEntity> page(int page, int pageSize, String name) {
        Page<SysUserEntity> sysUserEntityPage = dao.selectPage(getEmptyPage(page, pageSize),
                normalStateLambdaQueryWrapper().lambda()
                        .eq(StringUtils.isNotBlank(name), SysUserEntity::getUsername, name)
        );
        return PageResult.create(sysUserEntityPage);
    }

    @Override
    public List<SysUserEntity> findAll(String name) {
        return dao.selectList(
                normalStateLambdaQueryWrapper().lambda()
                    .eq(StringUtils.isNotBlank(name), SysUserEntity::getUsername, name)
        );
    }

    @Override
    public SysUserEntity findById(Long id) {
        return defaultFindOne(id);
    }

    @Override
    public SysUserEntity add(SaveUserBO user) {
        SM2 sm2 = SmUtil.sm2(privateKey, null);
        String userName = sm2.decryptStr(user.getUsername(), KeyType.PrivateKey);
        SysUserEntity sysUserEntity = findByUserName(userName);
        Assert.isNull(sysUserEntity,"已存在此用户，请重新注册");
        Assert.notNull(user.getSavePass(),"保存用户密码不能为空");
        SysUserEntity finalUser = saveInfo(user,true,e -> {
            String salt = UUIDUtil.genUUID();
            e.setSaltValue(salt);
            String password = user.getSavePass();
            String decoderPass = sm2.decryptStr(password, KeyType.PrivateKey);
            String finalPass = LoginConstants.createSm3Pass(e.getUsername(), salt, decoderPass);
            e.setPassword(finalPass);
            return e;
        });
        user.setSavePass(null);
        SysUserEntity saveResult = defaultSave(finalUser, true);
        if(saveResult != null){
            roleUserService.bindUserAndRole(user.getId(),user.getRoles());
        }
        return saveResult;
    }

    @Override
    public SysUserEntity update(SaveUserBO old) {
        SysUserEntity sysUserEntity = null;
        try {
            sysUserEntity = defaultFindOne(old.getId());
            Assert.notNull(sysUserEntity, "修改用户不存在");
            sysUserEntity.setComment(old.getComment());
            sysUserEntity.setVerifyCode(old.getVerifyCode());
            sysUserEntity.setEmail(old.getEmail());
            sysUserEntity = defaultUpdate(sysUserEntity);
            if(sysUserEntity != null){
                roleUserService.bindUserAndRole(sysUserEntity.getId(),old.getRoles());
            }
        }catch (IllegalArgumentException e){
            throw e;
        }
        return sysUserEntity;
    }

    @Override
    public boolean del(Long id) {
        return CollectionUtils.isNotEmpty(defaultLogicDelete(id));
    }

    @Override
    public boolean modifyPassword(ModifyPassBO modifyPassBO) {
        SM2 sm2 = SmUtil.sm2(privateKey, null);
        Long id = modifyPassBO.getId();
        SysUserEntity byId = findById(id);
        Assert.notNull(byId,"修改密码用户不存在");
        String saltValue = byId.getSaltValue();
        String password = byId.getPassword();
        String operatorName = byId.getUsername();

        String oldPass = modifyPassBO.getOldPass();
        String newPass = modifyPassBO.getNewPass();
        String newConfirmPass = modifyPassBO.getComfirmPass();
        String decoderOldPass = sm2.decryptStr(oldPass, KeyType.PrivateKey);
        String decoderNewPass = sm2.decryptStr(newPass, KeyType.PrivateKey);
        String decoderConfirmPass = sm2.decryptStr(newConfirmPass, KeyType.PrivateKey);

        if(!decoderNewPass.equals(decoderConfirmPass)){
            ServiceException.throwError(SystemErrorCode.ERROR_50002);
        }

        String sm3Pass = LoginConstants.createSm3Pass(operatorName, saltValue, decoderOldPass);
        if(!sm3Pass.equals(password)){
            ServiceException.throwError(SystemErrorCode.ERROR_50001);
        }

        String sm3NewPass = LoginConstants.createSm3Pass(operatorName, saltValue, decoderNewPass);
        if(sm3NewPass.equals(password)){
            ServiceException.throwError(SystemErrorCode.ERROR_50006);
        }

        String newSalt = UUIDUtil.genUUID();
        byId.setSaltValue(newSalt);
        byId.setPassword(LoginConstants.createSm3Pass(operatorName, newSalt, decoderNewPass));
        byId.setLastPswTime(DateUtil.getDateTime());
        return defaultUpdate(byId) != null;
    }

    @Override
    public boolean modifyLastLoginTime(Long userId, String time) {
        SysUserEntity byId = findById(userId);
        if(byId == null){
            Assert.notNull(byId,"修改密码用户不存在");
        }
        byId.setLastLoginTime(time);
        defaultUpdate(byId);
        return false;
    }

    @Override
    public SysUserEntity findByUserName(String username) {
        if (StringUtils.isNotBlank(username)) {
            return dao.selectOne(normalStateLambdaQueryWrapper().lambda().eq(SysUserEntity::getUsername, username));
        }
        return null;
    }

}
```



## 部署
程序中提供了docker-compose.yml，用于构建scs项目容器的构建和管理。Dockerfile用于将项目构建成为Docker镜像。
按照如下的结构放置文件，如图：
 ![image](https://github.com/univspeed/sprbot-freedom/assets/23521638/19a5cd46-29e8-4894-a4a5-47741105ba94)
 
```
docker-compose build 构建镜像
docker-compose up -d 启动容器
```


